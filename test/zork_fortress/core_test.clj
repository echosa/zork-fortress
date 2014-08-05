(ns zork-fortress.core-test
  (:require [clojure.test :refer :all]
            [zork-fortress.core :refer :all]))

(def test-game
  {:player {:name "Player"}})

(deftest test-default-prompt
  (testing "Default prompt should be '>'."
    (is (= "Player | > " (get-user-prompt test-game)))))

(deftest test-parsing-input-without-arguments
  (testing "Input should be parsed as command."
    (is (= {:trigger 'look} (parse-input "look")))))

(deftest test-parsing-input-with-arguments
  (testing "Input should be parsed into command and arguments."
    (is (= {:trigger 'history :args ["4"]}
           (parse-input "history 4")))))

