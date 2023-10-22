(ns net.bjoc.advent.year-2016.day-4
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.cipher :as cipher]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(def room-pattern #"^(.*)-(\d+)\[(.{5})\]")

(defn parse-line [line]
  (let [[_ name sector checksum] (re-find room-pattern line)]
    {:line line
     :name name
     :sector (parse-long sector)
     :checksum checksum}))

(defn name->checksum [name]
  (->> (-> name (str/replace #"-" "") frequencies)
       (sort-by (fn [[c n]] [(- n) c]))
       (map first)
       (take 5)
       str/join))

(defn valid? [{:keys [name checksum]}]
  (= (name->checksum name) checksum))

(defn room-sum [filename]
  (->> filename
       file->lines
       (map parse-line)
       (filter valid?)
       (map :sector)
       (reduce +)))

;;;

(defn decipher-name [{:keys [name sector]}]
  (let [f (cipher/rot-n-fn sector)]
    (->> name
         (map #(if (= % \-) \space (f %)))
         str/join)))

(def assoc-deciphered-name #(assoc % :deciphered-name (decipher-name %)))

(defn find-northpole-object [filename]
  (->> filename
       file->lines
       (map parse-line)
       (filter valid?)
       (map assoc-deciphered-name)
       (filter #(re-find #"northpole object" (:deciphered-name %)))
       first
       :sector))

;;;

(advent/defrunner 1 room-sum "Sector sum of valid rooms")
(advent/defrunner 2 find-northpole-object "Sector of North Pole Objects")
