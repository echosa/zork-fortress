(ns zork-fortress.types
  (:require [clojure.core.typed :as t]))
  
(t/defalias Player
  "The player."
  (t/HMap :mandatory {:name String}))
