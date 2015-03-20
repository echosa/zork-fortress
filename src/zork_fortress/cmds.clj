(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:use [zork-fortress.cmds.help :only [help-cmd]]
        [zork-fortress.cmds.history :only [history-cmd show-turn-in-history]]
        [zork-fortress.cmds.look :only [look-cmd]]
        [zork-fortress.cmds.inventory :only [inventory-cmd]]
        [zork-fortress.cmds.chop :only [chop-cmd chop-cmd-effects]]))

(t/ann get-new-last-turn [t2/Game t2/Command -> t2/Turn])
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

(t/ann get-new-turn-history [t2/Game -> (t/Vec (t/Option t2/Turn))])
(defn get-new-turn-history
  "Generate a new turn history for the game."
  [game]
  (filterv #(some? %) (conj (:turn-history game) (show-turn-in-history (:last-turn game)))))

(t/ann perform-command-effects [t2/Game t2/Command -> t2/Game])
(defn perform-command-effects
  "Update the game with the effects of the command."
  [game command]
  (let [args (:args command)]
    (condp = (:trigger command)
      'chop (chop-cmd-effects game args)
      game)))

(t/ann run-cmd [t2/Game (t/Option t2/Command) -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (if (nil? command)
    game
    (merge (perform-command-effects game command) 
           {:last-turn (get-new-last-turn game command)
            :turn-history (get-new-turn-history game)})))


