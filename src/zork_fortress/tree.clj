(ns zork-fortress.tree
  (:require [clojure.core.typed :as t]
            [zork-fortress.types :as t2]))

(t/ann is-treetypegroup-of-type [t2/TreeTypeGroup String -> t/Bool])
(defn is-treetypegroup-of-type
  "Returns true if the type of the given TreeTypeGroup is the given type."
  [tree-type-coll tree-type]
  (= (:type tree-type-coll) tree-type))

(t/ann filter-trees-of-type [(t/Vec t2/TreeTypeGroup) String -> (t/Vec t2/TreeTypeGroup)])
(defn filter-trees-of-type
  "Returns a collection of TreeTypeGroup matching the given type."
  [trees tree-type]
  (filterv (t/fn [tree-type-coll :- t2/TreeTypeGroup]
             (is-treetypegroup-of-type tree-type-coll tree-type))
           trees))

(t/ann tree-has-id [t2/Tree t/Int -> t/Bool])
(defn tree-has-id
  "Returns true if the tree has the given id."
  [tree id]
  (= (:id tree) id))

(t/ann filter-trees-with-id [(t/Vec t2/Tree) t/Int -> (t/Vec t2/Tree)])
(defn filter-trees-with-id
  "Returns trees that match the given id."
  [trees id]
  (filterv (t/fn [tree :- t2/Tree]
             (tree-has-id tree id))
           trees))

