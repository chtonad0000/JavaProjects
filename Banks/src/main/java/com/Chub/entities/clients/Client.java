package com.Chub.entities.clients;

import com.Chub.entities.clients.notifiers.BaseNotifier;
import lombok.Setter;

/**
 * Represents a client of the bank.
 */
public class Client {

    /**
     * Constructs a new client with the specified attributes.
     *
     * @param name the name of the client
     * @param surname the surname of the client
     * @param passportNumber the passport number of the client
     * @param address the address of the client
     */
    private Client(String name, String surname, String passportNumber, String address) {
        Name = name;
        Surname = surname;
        PassportNumber = passportNumber;
        Address = address;
    }

    /**
     * Sets the notifier for sending messages.
     */
    @Setter
    private BaseNotifier notifier = null;

    /**
     * The name of the client.
     */
    public final String Name;

    /**
     * The surname of the client.
     */
    public final String Surname;

    /**
     * The passport number of the client.
     */
    @Setter
    public String PassportNumber;

    /**
     * The address of the client.
     */
    @Setter
    public String Address;

    /**
     * Notifies the client using the assigned notifier.
     *
     * @param message the message to be sent
     */
    public void Notify(String message) {
        notifier.Send(message);
    }

    /**
     * Checks if the client's passport number and address are set.
     *
     * @return true if either passport number or address is null, false otherwise
     */
    public boolean Status() {
        return (PassportNumber == null || Address == null);
    }

    /**
     * Represents a builder for constructing clients.
     */
    public static class ClientBuilder {
        private String _name = null;
        private String _surname = null;
        private String _passportNumber = null;
        private String _address = null;

        /**
         * Sets the name of the client.
         *
         * @param name the name of the client
         */
        public void AddName(String name) {
            _name = name;
        }

        /**
         * Sets the surname of the client.
         *
         * @param surname the surname of the client
         */
        public void AddSurname(String surname) {
            _surname = surname;
        }

        /**
         * Sets the passport number of the client.
         *
         * @param passportNumber the passport number of the client
         */
        public void AddPassport(String passportNumber) {
            _passportNumber = passportNumber;
        }

        /**
         * Sets the address of the client.
         *
         * @param address the address of the client
         */
        public void AddAddress(String address) {
            _address = address;
        }

        /**
         * Constructs a new client using the provided information.
         *
         * @return a new client object
         */
        public Client Build() {
            return new Client(_name, _surname, _passportNumber, _address);
        }
    }
}

