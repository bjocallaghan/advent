(ns net.bjoc.advent.core
  (:use [clojure.java.shell :only [sh]])
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre
             ;; :refer [log  trace  debug  info  warn  error  fatal  report
             ;;         logf tracef debugf infof warnf errorf fatalf reportf
             ;;         spy get-env]])
             :refer [trace debug info warn error fatal]]
            ))

(def this-ns-stem (->> *ns*
                       ns-name
                       name
                       (re-find #"(.*)\..*?$")
                       second))

(defn run
  "Entry point to Advent of Code runner."
  [{:keys [year day] :as opts}]
  (debug "Options:" opts)
  (let [ns-symbol (symbol (format "%s.year-%s.day-%s" this-ns-stem year day))]
    (require ns-symbol)
    (time
     (let [m (-> ns-symbol find-ns ns-map)]
       ((m 'part-1))
       ((m 'part-2))))))

(defmacro defrunner
  [part-number fn-name description & additional-args]
  (let [[_ year day] (re-find #"year-(\d+)\.day-(\d+)$" (str (ns-name *ns*)))
        filename (format "data/year_%s/day_%s.input" year day)
        desc (format "%s - Day %s, Part %s - %s:"
                     year day part-number description)
        part-sym (symbol (format "part-%s" part-number))]
    (list 'defn part-sym '[]
          (list 'println desc
                (-> additional-args
                    (conj filename)
                    (conj fn-name))))))
