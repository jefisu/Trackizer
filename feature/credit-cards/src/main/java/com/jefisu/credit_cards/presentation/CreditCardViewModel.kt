package com.jefisu.credit_cards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.credit_cards.domain.CardConstants
import com.jefisu.credit_cards.domain.validation.cardCvvValidate
import com.jefisu.credit_cards.domain.validation.cardExpirationDateValidate
import com.jefisu.credit_cards.domain.validation.cardHolderValidate
import com.jefisu.credit_cards.domain.validation.cardNameValidate
import com.jefisu.credit_cards.domain.validation.cardNumberValidate
import com.jefisu.credit_cards.presentation.util.asCardFlag
import com.jefisu.credit_cards.presentation.util.asMessageText
import com.jefisu.designsystem.util.MessageController
import com.jefisu.designsystem.util.UiEventController
import com.jefisu.designsystem.util.formatExpirationDate
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.CardType
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.steliospapamichail.creditcardmasker.utils.getCardTypeFromNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreditCardViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val subscriptionRepository: SubscriptionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CreditCardState())
    val state = combine(
        _state,
        subscriptionRepository.subscriptions,
        cardRepository.cards,
    ) { state, subscriptions, cards ->
        val cardsMap = cards
            .filter { it.type == CardType.CREDIT }
            .associateWith { card ->
                subscriptions
                    .filter { it.cardId == card.id }
                    .map { it.service }
            }

        state.copy(
            creditCards = cardsMap,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _state.value,
    )

    fun onAction(action: CreditCardAction) {
        when (action) {
            is CreditCardAction.SelectCreditCard -> _state.update {
                it.copy(selectedCard = action.card)
            }

            is CreditCardAction.ToogleAddCreditCardBottomSheet -> _state.update {
                val date = action.card?.expirationDate
                    ?.formatExpirationDate()
                    ?.replace("/", "")
                    .orEmpty()

                it.copy(
                    showAddCreditCardBottomSheet = !it.showAddCreditCardBottomSheet,
                    editCard = action.card,
                    cardName = action.card?.name.orEmpty(),
                    cardHolder = action.card?.cardHolder.orEmpty(),
                    cardNumber = action.card?.number.orEmpty(),
                    cvv = action.card?.cvv.orEmpty(),
                    expirationDate = date,
                )
            }

            is CreditCardAction.CardNameChanged -> _state.update {
                it.copy(cardName = action.cardName)
            }

            is CreditCardAction.CardHolderChanged -> _state.update {
                it.copy(cardHolder = action.cardHolder)
            }

            is CreditCardAction.CardNumberChanged -> _state.update {
                val flag = getCardTypeFromNumber(action.cardNumber).asCardFlag()
                it.copy(
                    cardNumber = action.cardNumber,
                    flag = flag,
                )
            }

            is CreditCardAction.CvvCodeChanged -> _state.update {
                it.copy(cvv = action.cvv)
            }

            is CreditCardAction.ExpirationDateChanged -> _state.update {
                it.copy(expirationDate = action.expirationDate)
            }

            is CreditCardAction.SaveCard -> saveCard()
        }
    }

    private fun saveCard() {
        viewModelScope.launch {
            with(_state.value) {
                val nameResult = cardNameValidate.validate(cardName)
                val holderResult = cardHolderValidate.validate(cardHolder)
                val numberResult = cardNumberValidate.validate(cardNumber)
                val expirationDateResult = cardExpirationDateValidate.validate(expirationDate)
                val cvvResult = cardCvvValidate.validate(cvv)
                val errors = listOfNotNull(
                    nameResult.error,
                    holderResult.error,
                    numberResult.error,
                    expirationDateResult.error,
                    cvvResult.error,
                )
                if (errors.isNotEmpty()) {
                    MessageController.sendMessage(errors.first().asMessageText())
                    return@launch
                }

                val card = Card(
                    id = editCard?.id.orEmpty(),
                    name = cardName,
                    cardHolder = cardHolder,
                    number = cardNumber,
                    expirationDate = LocalDate.parse(
                        "01$expirationDate",
                        DateTimeFormatter.ofPattern(CardConstants.EXPIRATION_DATE_PATTERN),
                    ),
                    cvv = state.value.cvv,
                    flag = getCardTypeFromNumber(cardNumber).asCardFlag(),
                    type = CardType.CREDIT,
                )
                cardRepository.saveCard(card)
                    .onSuccess {
                        _state.update { it.copy(editCard = null) }
                        UiEventController.sendEvent(CreditCardUiEvent.HideBottomSheet)
                    }
                    .onError { message ->
                        MessageController.sendMessage(message as MessageText.Error)
                    }
            }
        }
    }
}
