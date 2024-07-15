package com.Chub.entities.clients.notifiers;

/**
 * Represents a base notifier class for sending messages.
 */
public class BaseNotifier {

    /**
     * Sends the specified message.
     *
     * @param message the message to be sent
     * @return the send message
     */
    public String Send(String message) {
        return message;
    }
}