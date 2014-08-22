(ns zork-fortress.cmds.help
  (:require [clojure.core.typed :as t]))

(t/ann default-help String)
(def default-help 
  "Which command you like help on? Try \"help <command>\" or \"help commands\" to see a list of commands.")

(t/ann help-cmd [-> String])
(defn help-cmd
  "Shows helpful information to the user."
  []
  default-help)