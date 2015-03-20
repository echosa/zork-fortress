(ns zork-fortress.game
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]
            [zork-fortress.area :as a]))

(t/ann get-new-game [-> t2/Game])
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

(t/ann get-current-area [t2/Game -> t2/Area])
(defn get-current-area
  "Returns the current area the player is in."
  [game]
  (a/get-area-from-game game (:current-area game)))

(t/ann tree-count-reduction [(t/Map String t/Int) t2/TreeTypeGroup -> (t/Map String t/Int)])
(defn- tree-count-reduction
  "Used as part of (get-current-area-tree-counts)."
  [val next]
  (merge val {(:type next) (count (:trees next))}))

(t/ann get-current-area-tree-counts [t2/Game -> (t/Map String t/Int)])
(defn get-current-area-tree-counts
  "Return a map of maps with tree counts for the current area."
   [game]
   (let [trees (:trees (get-current-area game))]
     (reduce tree-count-reduction
             {}
             trees)))
