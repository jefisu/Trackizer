# Keeps classes implementing FirestoreDocumentSync intact
# to prevent serialization and deserialization issues with Firebase Firestore
-keep class * implements com.jefisu.data.remote.document.FirestoreDocumentSync {
    *;
}