(ns zork-fortress.game-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]))

(deftest test-getting-current-area-from-game
  (testing "Getting current area from game should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (g/get-current-area game))))))

(deftest test-getting-current-area-tree-counts
  (testing "Should get correct tree counts from game."
    (let [game (h/get-test-game)]
      (is (= {"oak" 1}
             (g/get-current-area-tree-counts game))))))

