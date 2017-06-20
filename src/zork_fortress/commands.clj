(ns zork-fortress.commands
  (:use [zork-fortress.commands.help :only [help-cmd]]
        [zork-fortress.commands.history :only [history-cmd show-turn-in-history]]
        [zork-fortress.commands.look :only [look-cmd]]
        [zork-fortress.commands.inventory :only [inventory-cmd]]
        [zork-fortress.commands.chop :only [chop-cmd chop-cmd-effects]]))

(defn get-new-last-turn
  "Generate a new last turn entry for the game."
  [game command]
  (let [args (:args command)]
    (merge {:command command}
           (condp = (:trigger command)
             'help {:response (help-cmd args)}
             'history {:response (history-cmd game args)}
             'look {:response (look-cmd game)}
             'chop {:response (chop-cmd game args)}
             'inventory {:response (inventory-cmd game)}
             {:response "Invalid command." :invalid true}))))

(defn get-new-turn-history
  "Generate a new turn history for the game."
  [game]
  (filterv #(some? %) (conj (:turn-history game) (show-turn-in-history (:last-turn game)))))

(defn perform-command-effects
  "Update the game with the effects of the command."
  [game command]
  (let [args (:args command)]
    (condp = (:trigger command)
      'chop (chop-cmd-effects game args)
      game)))

(defn run-cmd
  "Run the given command."
  [game command]
  (if (nil? command)
    game
    (merge (perform-command-effects game command)
           {:last-turn (get-new-last-turn game command)
            :turn-history (get-new-turn-history game)})))


