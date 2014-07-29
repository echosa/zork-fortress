(ns zork-fortress.core-test
  (:require [clojure.test :refer :all]
            [zork-fortress.core :refer :all]))

(deftest test-default-prompt
  (testing "Default prompt should be '>'."
    (is (= "Player 1 | > " (get-user-prompt {:name "Player 1"})))))
