(ns zork-fortress.commands.inventory
  (:require [zork-fortress.game :as g]
            [zork-fortress.inventory :as i]
            [clojure.string :as str]))

(def empty-inventory-msg
  "You aren't carrying anything.")

(defn get-log-counts
  "Returns display messages for the player's log counts."
  [inv]
  (let [logs (:logs inv)]
    (str/join "\n" (for [log logs]
                          (when (not= 0 (:count log))
                            (str (:count log) " " (:type log)
                                 (if (= 1 (:count log)) " log." " logs.")))))))

(defn inventory-command
  "The inventory command."
  [game]
  (let [inv (i/get-player-inventory game)]
    (if (empty? inv)
      empty-inventory-msg
      (get-log-counts inv))))
