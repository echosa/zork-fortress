(ns zork-fortress.commands.help-test
  (:require [clojure.test :refer :all]
            [zork-fortress.test-helpers :as h]
            [zork-fortress.game :as g]
            [zork-fortress.commands.help :as help])
  (:use [zork-fortress.commands :only [run-cmd]]))

(deftest test-default-help-should-prompt-for-command
  (testing "Typing 'help' without an argument should prompt for argument."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'help})))
                     help/default-help)))))

(deftest test-help-for-invalid-command-should-tell-user
  (testing "Asking for help on an invalid command should tell the user as such."
    (let [game (h/get-test-game)]
      (is (.contains (:response (:last-turn (run-cmd game {:trigger 'help :args ["foobar"]})))
                     help/invalid-command-msg)))))

(deftest test-help-commands-should-provide-command-list
  (testing "Running 'help commands' should provide a list of commands for which help is available."
    (let [game (h/get-test-game)
          response (:response (:last-turn (run-cmd game {:trigger 'help :args ["commands"]})))]
      (is (.contains response "look"))
      (is (.contains response "help"))
      (is (.contains response "history")))))

(deftest test-help-commands-should-not-include-test-namespaces
  (testing "Running 'help commands' should not include test namespaces in its output."
    (let [game (h/get-test-game)
          response (:response (:last-turn (run-cmd game {:trigger 'help :args ["commands"]})))]
      (is (.contains response "look"))
      (is (.contains response "help"))
      (is (.contains response "history")))))

(deftest test-help-commands-should-be-alphabetical
  (testing "Running 'help commands' should provide an alphabetical list."
    (let [game (h/get-test-game)
          response (:response (:last-turn (run-cmd game {:trigger 'help :args ["commands"]})))]
      (is (< (.indexOf response "help") (.indexOf response "history")))
      (is (< (.indexOf response "history") (.indexOf response "look"))))))

(deftest test-help-should-show-help-for-requested-command
  (testing "Giving a command to 'help' should display its help info."
    (let [game (h/get-test-game)]
      (is (= zork-fortress.commands.look/look-cmd-help (:response (:last-turn (run-cmd game {:trigger 'help :args ["look"]})))))
      (is (= zork-fortress.commands.history/history-cmd-help (:response (:last-turn (run-cmd game {:trigger 'help :args ["history"]}))))))))