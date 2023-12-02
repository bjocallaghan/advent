(ns net.bjoc.advent.year-2023.day-2
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(declare str->draw)

(defn line->summary [line]
  (let [[_ raw-game-num body] (re-find #"^Game (\d+): (.*)$" line)
        raw-draws (str/split body #"; ")]
    {:line line
     :game-id (parse-long raw-game-num)
     :draws (map str->draw raw-draws)}))

(defn str->draw [s]
  (->> (str/split s #", ")
       (map #(let [[a b] (str/split % #" ")]
               {b (parse-long a)}))
       (apply merge)))

(defn max-simultaneous-color [color summary]
  (->> summary
       :draws
       (map #(get % color 0))
       (concat [0])
       (apply max)))

(def max-simultaneous-green (partial max-simultaneous-color "green"))
(def max-simultaneous-blue (partial max-simultaneous-color "blue"))
(def max-simultaneous-red (partial max-simultaneous-color "red"))

(def possible? #(and (>= 12 (max-simultaneous-red %))
                     (>= 13 (max-simultaneous-green %))
                     (>= 14 (max-simultaneous-blue %))))

(defn file->score [file]
  (->> file
       file->lines
       (map line->summary)
       (filter possible?)
       (map :game-id)
       (reduce +)))

;;;

(def summary->power #(reduce * [(max-simultaneous-red %)
                                (max-simultaneous-green %)
                                (max-simultaneous-blue %)]))

(defn file->power [file]
  (->> file
       file->lines
       (map line->summary)
       (map summary->power)
       (reduce +)))
;;;

(advent/defrunner 1 file->score "Sum of possible game-ids")
(advent/defrunner 2 file->power "Sum of set powers")
