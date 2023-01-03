(ns net.bjoc.advent.year-2022.day-18
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords]]
            [net.bjoc.advent.util.misc :refer [file->lines find-first]]))

(defn line->cube [line]
  (->> (str/split line #",")
       (map #(Integer/parseInt %))))

(defn neighbors [xyz]
  (set (map (partial add-coords xyz)      
            [[1 0 0] [0 1 0] [0 0 1] [-1 0 0] [0 -1 0] [0 0 -1]])))

(defn file->cubes [filename]
  (->> filename
       file->lines
       (map line->cube)
       set))

(defn vacant-neighbors [cubes cube]
  (remove cubes
          (neighbors cube)))

(defn num-exposed-faces [cubes]
  (->> (set cubes)
       (map (partial vacant-neighbors cubes))
       (map count)
       (reduce +)))

(def file->num-exposed-faces (comp num-exposed-faces file->cubes))

;;;

(defn cubes->islands
  "Partition a set of `cubes` into 'islands': sets of contiguous cubes."
  [cubes]
  (loop [cubes (set cubes)
         frontier []
         islands []
         visited #{}
         island #{}]
    (cond
      (empty? frontier) (recur (set/difference cubes island)
                               #{(first (set/difference cubes island))}
                               (conj islands island)
                               #{}
                               #{})
      (empty? cubes) (map set (rest islands))
      :else (recur cubes
                   (-> (mapcat neighbors frontier)
                       set
                       (set/difference visited)
                       (set/intersection cubes))
                   islands
                   (set/union visited frontier)
                   (set/union island frontier)))))

(defn num-exterior-faces [cubes]
  (let [shell-1 (-> (mapcat neighbors cubes)
                    set
                    (set/difference cubes))
        shell-2 (-> (mapcat neighbors shell-1)
                    set
                    (set/difference cubes)
                    (set/difference shell-1))
        shell-1-2 (set/union shell-1 shell-2)
        sorted-islands (->> (cubes->islands shell-1-2)
                            (sort-by (comp - count)))
        outer-shell (first sorted-islands)]
    (->> (mapcat neighbors cubes)
         (filter outer-shell)
         count)))

(def file->num-exterior-faces (comp num-exterior-faces file->cubes))

;;;

(advent/defrunner 1 file->num-exposed-faces "Exposed faces")
(advent/defrunner 2 file->num-exterior-faces "Exterior faces")
