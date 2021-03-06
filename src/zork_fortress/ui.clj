(ns zork-fortress.ui
  (:require [clojure.string :as str]
            [zork-fortress.player :as p]
            [zork-fortress.commands :as commands]))

(defn welcome-message
  "Print a welcome message for the user."
  []
  "Welcome! Try typing 'look'.")

(defn get-user-prompt
  "Return the correct current prompt for the user."
  [game]
  (str (p/get-player-name game) " | > "))

(defn print-prompt
  "Prints the user prompt."
  [game]
  (print (get-user-prompt game)))

(defn print-last-response
  "Prints the response from the previous turn."
  [game]
  (let [last-response (-> game :last-turn :response)]
    (when last-response
      (println last-response))))

(defn process-user-input
  "Process the user's input and loops."
  [game]
  (let [user-input (or (read-line) "")]
    (when (not= user-input "quit")
      (commands/run-command game user-input))))
