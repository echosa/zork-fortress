(ns zork-fortress.game
  (:require [zork-fortress.area :as a]))

(defn get-new-game
  "Returns a new, initialized game."
  []
  (let [area {:id 1
              :name "Starting Area"
              :type "plains"
              :trees [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}]}]
    {:player {:name "Player" :inventory {}}
     :world {:areas [area]}
     :current-area 1
     :turn-history []}))

(defn get-current-area
  "Returns the current area the player is in."
  [game]
  (a/get-area-from-game game (:current-area game)))

(defn- tree-count-reduction
  "Used as part of (get-current-area-tree-counts)."
  [val next]
  (merge val {(:type next) (count (:trees next))}))

(defn get-current-area-tree-counts
  "Return a map of maps with tree counts for the current area."
   [game]
   (let [trees (:trees (get-current-area game))]
     (reduce tree-count-reduction
             {}
             trees)))
