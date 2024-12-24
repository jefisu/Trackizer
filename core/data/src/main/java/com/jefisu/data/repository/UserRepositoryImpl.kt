package com.jefisu.data.repository

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.data.remote.image.ImageUploader
import com.jefisu.data.util.ImageCompressor
import com.jefisu.data.util.safeCallResult
import com.jefisu.data.util.userFlow
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.User
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val app: Application,
    private val realm: Realm,
    private val dispatcherProvider: DispatcherProvider,
    private val imageUploader: ImageUploader,
    private val imageCompressor: ImageCompressor,
) : UserRepository {

    private val auth = Firebase.auth

    override val user: Flow<User?> = auth.userFlow().map { firebaseUser ->
        if (firebaseUser == null) return@map null
        User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName,
            email = firebaseUser.email ?: "No email",
            pictureUrl = firebaseUser.photoUrl?.toString(),
        )
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
        realm.write { deleteAll() }
    }

    override suspend fun deleteAccount(password: String): Result<Unit, DataMessage> {
        return safeCallResult(
            dispatcher = dispatcherProvider.io,
            exceptions = mapOf(
                FirebaseAuthInvalidCredentialsException::class to DataMessage.INCORRECT_PASSWORD,
                Exception::class to DataMessage.DELETE_ACCOUNT_FAILED,
            ),
        ) {
            val authResult = auth.signInWithEmailAndPassword(
                auth.currentUser?.email.orEmpty(),
                password,
            ).await()
            requireNotNull(authResult.user)

            auth.currentUser?.delete()?.await()
            launch { realm.write { deleteAll() } }
        }
    }

    override suspend fun updateProfile(
        name: String?,
        pictureUrl: String?,
    ): Result<Unit, DataMessage> {
        return safeCallResult(
            exceptions = mapOf(
                Exception::class to DataMessage.UPDATE_PROFILE_FAILED,
            ),
        ) {
            val currentUser = auth.currentUser
            val request = UserProfileChangeRequest.Builder()

            name?.let(request::setDisplayName)
            pictureUrl?.let {
                val contentUri = Uri.parse(it)
                val mimeType = app.contentResolver.getType(contentUri)
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                imageCompressor.compressImage(
                    contentUri = Uri.parse(it),
                    compressionThreshold = 32 * 1024,
                )?.let { compressedBytes ->
                    val remoteUrl = imageUploader.upload(
                        filename = "profile-${System.currentTimeMillis()}-${currentUser?.uid}.$extension",
                        data = compressedBytes,
                    )
                    request.setPhotoUri(Uri.parse(remoteUrl))
                }
            }

            currentUser?.updateProfile(request.build())?.await()
        }
    }
}