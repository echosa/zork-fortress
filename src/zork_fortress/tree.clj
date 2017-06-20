(ns zork-fortress.tree)

(defn is-treetypegroup-of-type
  "Returns true if the type of the given TreeTypeGroup is the given type."
  [tree-type-coll tree-type]
  (= (:type tree-type-coll) tree-type))

(defn filter-trees-of-type
  "Returns a collection of TreeTypeGroup matching the given type."
  [trees tree-type]
  (filterv (fn [tree-type-coll]
             (is-treetypegroup-of-type tree-type-coll tree-type))
           trees))

(defn tree-has-id
  "Returns true if the tree has the given id."
  [tree id]
  (= (:id tree) id))

(defn filter-trees-with-id
  "Returns trees that match the given id."
  [trees id]
  (filterv (fn [tree]
             (tree-has-id tree id))
           trees))

