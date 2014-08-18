(ns zork-fortress.game
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann get-area-from-game [t2/Game t/Int -> t2/Area])
(defn get-area-from-game
  "Returns the area in the game with the given ID."
  [game area-id]
  {:pre [(not (empty? (:areas (:world game))))]}
  (nth (filterv (fn [item] (= (:id item) area-id))
                (:areas (:world game)))
       0))

(t/ann get-current-area [t2/Game -> t2/Area])
(defn get-current-area
  "Returns the current area the player is in."
  [game]
  (get-area-from-game game (:current-area game)))

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

(t/ann add-tree-to-area [t2/Game t/Int String t/Int -> t2/Game])
(defn add-tree-to-area
  "Returns the game with a tree added to the given area."
  [game area-id tree-type log-count]
  (let [new-tree {:id 2 :type tree-type :log-count log-count}
        updated-area {:id 1
                      :name "First Area" 
                      :type "plains"
                      :trees [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                                   new-tree]}]}
        updated-world (merge (:world game) {:area updated-area})]
    (merge game {:world updated-world})))
