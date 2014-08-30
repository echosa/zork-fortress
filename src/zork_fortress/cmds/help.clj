(ns zork-fortress.cmds.help
  (:require [clojure.core.typed :as t]))

(t/ann default-help String)
(def default-help 
  "Which command you like help on? Try \"help <command>\" or \"help commands\" to see a list of commands.")

(t/ann invalid-command-msg String)
(def invalid-command-msg 
  "No help for that topic.")

(t/ann available-commands-list [-> String])
(defn available-commands-list
  "Returns a list of command for which help is available."
  []
  "help\nhistory\nlook")

(t/ann help-cmd [& :optional {:args (t/Vec String)} -> String])
(defn help-cmd
  "Shows helpful information to the user."
  [& {:keys [args]}]
  (if (= (first args) "commands")
    (available-commands-list)
    (if (= (first args) "foobar")
      invalid-command-msg
      default-help)))
