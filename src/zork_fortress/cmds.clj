(ns zork-fortress.cmds
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2])
  (:use [zork-fortress.cmds.history :only [history-cmd get-last-turn-for-history]]))

(t/ann look-cmd [t2/Game -> String])
(defn look-cmd
  "The look command."
  [game]
  "You see nothing.")

(t/ann get-new-last-turn [t2/Game t2/Command -> t2/Turn])
(defn get-new-last-turn
  "Generate a new last turn entry for the game."
  [game command]
  (let [args (when (:args command) (:args command))]
    (merge {:command command}
           (condp = (:trigger command)
             'look {:response (look-cmd game)}
             'history {:response (if (nil? args)
                                   (history-cmd game)
                                   (history-cmd game :args args))}
             {:response "Invalid command." :invalid true}))))

(t/ann get-new-turn-history [t2/Game -> (t/Vec (t/Option t2/Turn))])
(defn get-new-turn-history
  "Generate a new turn history for the game."
  [game]
  (let [last-turn-for-history (get-last-turn-for-history game)]
    (if last-turn-for-history
      (conj (:turn-history game) last-turn-for-history)
      (:turn-history game))))

(t/ann run-cmd [t2/Game t2/Command -> t2/Game])
(defn run-cmd
  "Run the given command."
  [game command]
  (merge game 
         {:last-turn (get-new-last-turn game command)
          :turn-history (get-new-turn-history game)}))


