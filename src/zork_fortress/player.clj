(ns zork-fortress.player)

(defn get-player-name
  "Returns the player's name."
  [game]
  (-> game :player :name))

