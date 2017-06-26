(ns zork-fortress.commands
  (:use [zork-fortress.commands.help :only [help-command]]
        [zork-fortress.commands.history :only [history-command show-turn-in-history]]
        [zork-fortress.commands.look :only [look-command]]
        [zork-fortress.commands.inventory :only [inventory-command]]
        [zork-fortress.commands.chop :only [chop-command chop-command-effects]])
  (:require [clojure.string :as str]))

(defn execute-command-or-alias
  "Executes a command or alias."
  [game command args]
  (condp = (:trigger command)
    'help {:response (help-command args)}
    '? {:response (help-command args)}
    'history {:response (history-command game args)}
    'look {:response (look-command game)}
    'l {:response (look-command game)}
    'chop {:response (chop-command game args)}
    'inventory {:response (inventory-command game)}
    'inv {:response (inventory-command game)}
    'i {:response (inventory-command game)}
    {:response "Invalid command." :invalid true}))

(defn get-new-last-turn
  "Generate a new last turn entry for the game."
  [game command]
  (let [args (:args command)]
    (merge {:command command}
           (execute-command-or-alias game command args))))

(defn get-new-turn-history
  "Generate a new turn history for the game."
  [game]
  (filterv #(some? %) (conj (:turn-history game) (show-turn-in-history (:last-turn game)))))

(defn perform-command-effects
  "Update the game with the effects of the command."
  [game command]
  (let [args (:args command)]
    (condp = (:trigger command)
      'chop (chop-command-effects game args)
      game)))

(defn parse-input
  "Parse the user input into a command."
  [input]
  (let [parsed-string (str/split input #"\s+")
        command (first parsed-string)
        args (subvec parsed-string 1)]
    (when (not (or (nil? command) (empty? command)))
      {:trigger (symbol command) :args args})))

(defn run-command
  "Run the given command."
  [game user-input]
  (let [command (parse-input user-input)]
    (if (nil? command)
      game
      (merge (perform-command-effects game command)
             {:last-turn    (get-new-last-turn game command)
              :turn-history (get-new-turn-history game)}))))


