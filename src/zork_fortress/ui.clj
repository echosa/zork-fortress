(ns zork-fortress.ui
  (:require [clojure.core.typed :as t]
            [clojure.string :as str]
            [zork-fortress.types :as t2]
            [zork-fortress.player :as p]
            [zork-fortress.cmds :as cmds]))

(t/ann welcome-message [-> String])
(defn welcome-message
  "Print a welcome message for the user."
  []
  "Welcome! Try typing 'look'.")

(t/ann get-user-prompt [t2/Game -> String])
(defn get-user-prompt
  "Return the correct current prompt for the user."
  [game]
  (str (p/get-player-name game) " | > "))
  
(t/ann print-prompt [t2/Game -> nil])
(defn print-prompt
  "Prints the user prompt."
  [game]
  (print (get-user-prompt game)))

(t/ann print-last-response [t2/Game -> nil])
(defn print-last-response
  "Prints the response from the previous turn."
  [game]
  (let [last-response (-> game :last-turn :response)]
    (when last-response
      (println last-response))))

(t/ann parse-input [String -> (t/Option t2/Command)])
(defn parse-input
  "Parse the user input into a command."
  [input]
  (let [parsed-string (str/split input #"\s+")
        command (first parsed-string)
        args (subvec parsed-string 1)]
    (when (not (or (nil? command) (empty? command)))
      {:trigger (symbol command) :args args})))

(t/ann process-user-input [t2/Game -> (t/Option t2/Game)])
(defn process-user-input
  "Process the user's input and loops."
  [game]
  (let [user-input (or (read-line) "")]
    (when (not= user-input "quit")
      (cmds/run-cmd game (parse-input user-input)))))
