(ns zork-fortress.game-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]))

(deftest test-getting-current-area-from-game
  (testing "Getting current area from game should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (g/get-current-area game))))))

(deftest test-getting-area-from-game
  (testing "Getting area from game by ID should return correct area"
    (let [game (h/get-test-game)]
      (is (= (h/get-test-area)
             (g/get-area-from-game game 1))))))

(deftest test-getting-current-area-tree-counts
  (testing "Should get correct tree counts from game."
    (let [game (h/get-test-game)]
      (is (= {"oak" 1}
             (g/get-current-area-tree-counts game))))))

(deftest test-adding-tree
  (testing "Correct tree should be added to the  correct area."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "oak" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}
                                   {:id 2 :type "oak" :log-count 5}]}]
             (:trees (:area (:world game))))))))

(deftest test-getting-tree-type
  (testing "Correct tree type listing should be returned."
    (let [game (h/get-test-game)]
      (is (= {:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
             (g/get-tree-type-from-area game 1 "oak"))))))

(deftest test-getting-next-tree-id
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 2 (g/get-next-tree-id game 1 "oak"))))))

(deftest test-getting-next-tree-id-for-nonexisting-type
  (testing "Correct id should be returned."
    (let [game (h/get-test-game)]
      (is (= 1 (g/get-next-tree-id game 1 "pine"))))))

(deftest test-getting-tree-types-without-type
  (testing "Should get tree type vector without given type."
    (let [game (h/get-test-game)]
      (is (= []
             (g/get-tree-types-from-area-without-type game 1 "oak"))))))

(deftest test-adding-different-tree-type
  (testing "Correct tree should be added to the  correct area."
    (let [game (h/get-test-game)
          game (g/add-tree-to-area game 1 "pine" 5)]
      (is (= [{:type "oak" :trees [{:id 1 :type "oak" :log-count 10}]}
              {:type "pine" :trees [{:id 1 :type "pine" :log-count 5}]}]
             (:trees (:area (:world game))))))))

