(ns zork-fortress.cmds.inventory
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]
            [zork-fortress.player :as p]
            [clojure.string :as str]))

(t/ann empty-inventory-msg String)
(def empty-inventory-msg
  "You aren't carrying anything.")

(t/ann get-log-counts [t2/PlayerInventory -> String])
(defn get-log-counts
  "Returns display messages for the player's log counts."
  [inv]
  (let [logs (:logs inv)]
    (str/join "\n" (t/for [log :- t2/InventoryLogTypeMap logs] :- (t/Option String)
                          (when (not= 0 (:count log))
                            (str (:count log) " " (:type log)
                                 (if (= 1 (:count log)) " log." " logs.")))))))

(t/ann inventory-cmd [t2/Game -> String])
(defn inventory-cmd
  "The inventory command."
  [game]
  (let [area (g/get-current-area game)
        inv (p/get-player-inventory game)]
    (if (empty? inv)
      empty-inventory-msg
      (get-log-counts inv))))
