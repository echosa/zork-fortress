(ns zork-fortress.core
  (:require [clojure.core.typed :as t]
            [clojure.string :as s]
            [zork-fortress.types :as t2]
            [zork-fortress.cmds :as cmds])
  (:gen-class))

(t/ann get-user-prompt [t2/Game -> String])
(defn get-user-prompt
  "Return the correct current prompt for the user."
  [game]
  (str (:name (:player game)) " | > "))
  
(t/ann welcome-message [-> String])
(defn welcome-message
  "Print a welcome message for the user."
  []
  "Welcome! Try typing `look`.")

(t/ann parse-input [String -> (t/Option t2/Command)])
(defn parse-input
  "Parse the user input into a command."
  [input]
  (let [parsed-string (s/split input #"\s+")
        command (first parsed-string)
        args (subvec parsed-string 1)]
    (when (not (or (nil? command) (empty? command)))
      (if (empty? args)
        {:command (symbol command)}
        {:command (symbol command) :args args}))))

(t/ann game-loop [t2/Game -> t/Any])
(defn game-loop
  "The main game loop."
  [game]
  (let [last-response (:response (:last-turn game))]
    (when last-response
      (println last-response)))
  (print (get-user-prompt game))
  (flush)
  (let [user-input (or (read-line) "")]
    (when-not (= user-input "quit")
      (let [command (parse-input user-input)]
        (when-not (nil? command)
          (game-loop (cmds/run-cmd game command)))))))

(t/ann get-new-game [-> t2/Game])
(defn get-new-game
  "Returns a new, initialized game."
  []
  {:player {:name "Player"}
   :world {:areas [{:name "Starting Area"
                    :type "Plains"
                    :trees [{:type "Oak" :log-count 10}]}]}
   :turn-history []})

(t/ann -main [t/Any -> t/Any])
(defn -main
  "Main entry point to start the game and get into the game loop."
  [& args]
  (println (welcome-message))
  (game-loop (get-new-game)))