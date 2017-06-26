(ns zork-fortress.commands-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h])
  (:use [zork-fortress.commands :only [run-command parse-input]]))

(deftest test-commands-should-set-last-turn
  (testing "Look command should return message."
    (let [game (h/get-test-game)]
      (is (= nil (:last-turn game))
          (= {:command {:trigger 'look} :response "You see nothing."}
             (:last-turn (run-command game "look")))))))

(deftest test-commands-should-set-history
  (testing "Look command should return message."
    (let [game (h/get-test-game :last-turn {:command {:trigger 'prev}
                                            :response "Previous command."})]
      (is (= [] (:turn-history game))
          (= [{:command {:trigger 'prev} :response "Previous command."}]
             (:turn-history (run-command game "look")))))))

(deftest test-invalid-commands-are-marked-as-such
  (testing "Invalid command should respond as such."
    (let [game (h/get-test-game)]
      (is (= {:command {:trigger 'foobar, :args []}
              :response "Invalid command."
              :invalid true}
              (:last-turn (run-command game "foobar")))))))

(deftest test-parsing-input-without-arguments
  (testing "Input should be parsed as command."
    (is (= {:trigger 'look :args []} (parse-input "look")))))

(deftest test-parsing-input-with-arguments
  (testing "Input should be parsed into command and arguments."
    (is (= {:trigger 'history :args ["4"]}
           (parse-input "history 4")))))

