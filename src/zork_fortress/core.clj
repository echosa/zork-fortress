(ns zork-fortress.core
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:gen-class))
  
(t/ann get-user-prompt [t2/Player -> String])
(defn get-user-prompt
  "Return the correct current prompt for the user."
  [user]
  (str (:name user) " | > "))
  
(t/ann welcome-message [-> String])
(defn welcome-message
  "Print a welcome message for the user."
  []
  "Welcome! Try typing `look`.")
  
(t/ann game-loop [t2/Player -> t/Any])
(defn game-loop
  "The main game loop."
  [user]
  (print (get-user-prompt user))
  (flush)
  (let [user-input (read-line)]
    (when-not (= user-input "quit")
      (println user-input)
      (game-loop user))))

(t/ann -main [t/Any -> t/Any])
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (welcome-message))
  (game-loop {:name "Player"}))