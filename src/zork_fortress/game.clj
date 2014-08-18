(ns zork-fortress.game
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-current-area [t2/Game -> t2/Area])
(defn get-current-area
  "Returns the current area the player is in."
  [game]
  {:pre [(not (empty? (:areas (:world game))))]}
  (nth (filterv (fn [item] (= (:id item) (:current-area game)))
                (:areas (:world game)))
       0))

(t/ann tree-count-reduction [(t/Map String t/Int) (t/HMap :mandatory {:type String :trees (t/Vec t2/Tree)}) -> (t/Map String t/Int)])
(defn- tree-count-reduction
  "Used as part of (get-current-area-tree-counts)."
  [val next]
  {(:type next) (count (:trees next))})

(t/ann get-current-area-tree-counts [t2/Game -> (t/Map String t/Int)])
(defn get-current-area-tree-counts
  "Return a map of maps with tree counts for the current area."
   [game]
   (let [trees (:trees (get-current-area game))]
     (reduce tree-count-reduction
             {}
             trees)))
