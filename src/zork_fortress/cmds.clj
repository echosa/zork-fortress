(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:use [zork-fortress.cmds.help :only [help-cmd]]
        [zork-fortress.cmds.history :only [history-cmd get-last-turn-for-history]]
        [zork-fortress.cmds.look :only [look-cmd]]
        [zork-fortress.cmds.inventory :only [inventory-cmd]]
        [zork-fortress.cmds.chop :only [chop-cmd chop-cmd-effects]]))

(t/ann get-new-last-turn [t2/Game t2/Command -> t2/Turn])
(defn get-new-last-turn
  "Generate a new last turn entry for the game."
  [game command]
  (let [args (when (:args command) (:args command))]
    (merge {:command command}
           (condp = (:trigger command)
             'help {:response (if (nil? args)
                                   (help-cmd)
                                   (help-cmd :args args))}
             'history {:response (if (nil? args)
                                   (history-cmd game)
                                   (history-cmd game :args args))}
             'look {:response (look-cmd game)}
             'chop {:response (if (nil? args)
                                (chop-cmd game)
                                (chop-cmd game :args args))}
             'inventory {:response (inventory-cmd game)}
             {:response "Invalid command." :invalid true}))))

(t/ann get-new-turn-history [t2/Game -> (t/Vec (t/Option t2/Turn))])
(defn get-new-turn-history
  "Generate a new turn history for the game."
  [game]
  (let [last-turn-for-history (get-last-turn-for-history game)]
    (if last-turn-for-history
      (conj (:turn-history game) last-turn-for-history)
      (:turn-history game))))

(t/ann update-game [t2/Game t2/Command -> t2/Game])
(defn update-game
  "Update the game with the effects of the command."
  [game command]
  (let [args (when (:args command) (:args command))
        effected-game (when-not (nil? args)
                        (chop-cmd-effects game :args args)) 
        updated-game (if (nil? effected-game) game effected-game)]
    updated-game))

(t/ann run-cmd [t2/Game t2/Command -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (let [last-turn (get-new-last-turn game command)
        turn-history (get-new-turn-history game)
        updated-game (update-game game command)]
    (merge updated-game 
           {:last-turn last-turn
            :turn-history turn-history})))


