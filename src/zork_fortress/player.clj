(ns zork-fortress.player
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]))

(t/ann get-player-name [t2/Game -> String])
(defn get-player-name
  "Returns the player's name."
  [game]
  (-> game :player :name))

(t/ann get-player-inventory [t2/Game -> t2/PlayerInventory])
(defn get-player-inventory
  "Returns the player's inventory."
  [game]
  (-> game :player :inventory))
