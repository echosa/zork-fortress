(ns zork-fortress.player
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.game :as g]))

(t/ann get-player-inventory [t2/Game -> t2/PlayerInventory])
(defn get-player-inventory
  "Returns the player's inventory."
  [game]
  (:inventory (:player game)))
