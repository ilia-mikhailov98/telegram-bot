package study.telegrambot.logic;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import study.telegrambot.property.config.BotProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;

    private final Random random = new Random();

    private final String ROCK_UNICODE = "\uD83E\uDEA8";
    private final String SCISSORS_UNICODE = "✂️";
    private final String PAPER_UNICODE = "\uD83D\uDCC3";

    private final List<String> variants = Arrays.asList(ROCK_UNICODE, SCISSORS_UNICODE, PAPER_UNICODE);

    public TelegramBot(BotProperties botProperties) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            long chatId = message.getChatId();
            String userName = message.getFrom().getFirstName();

            String answer = choose();

            switch (text) {
                case "/start": {
                    sendMessage(chatId, String.format("Привет, %s, давай поиграем! Выбери, чем ты хочешь походить:", userName));
                    break;
                }
                case ROCK_UNICODE:
                case SCISSORS_UNICODE:
                case PAPER_UNICODE: {
                    sendMessage(chatId, answer);
                    sendMessage(chatId, whoWins(text, answer));
                    break;
                }
                default: {
                    sendMessage(chatId, "Прости, я тебя не понимаю... Может, попробуешь еще раз?");
                    break;
                }
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(ROCK_UNICODE);
        row.add(SCISSORS_UNICODE);
        row.add(PAPER_UNICODE);

        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String choose() {
        return variants.get(random.nextInt(variants.size()));
    }

    private String whoWins(String playerOption, String botOption) {
        if (playerOption.equals(botOption)) {
            return "Ничья!";
        } else if ((playerOption.equals(ROCK_UNICODE) && botOption.equals(SCISSORS_UNICODE)) || (playerOption.equals(SCISSORS_UNICODE) && botOption.equals(PAPER_UNICODE)) || (playerOption.equals(PAPER_UNICODE) && botOption.equals(ROCK_UNICODE))) {
            return "Ты победил!";
        } else {
            return "На этот раз победил я :)";
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }
}