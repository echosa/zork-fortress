(ns zork-fortress.core
  (:require [clojure.core.typed :as t]
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
      (game-loop (cmds/run-cmd game (symbol user-input))))))

(t/ann get-new-game [-> t2/Game])
(defn get-new-game
  "Returns a new, initialized game."
  []
  {:player {:name "Player"}})

(t/ann -main [t/Any -> t/Any])
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (welcome-message))
  (game-loop (get-new-game)))