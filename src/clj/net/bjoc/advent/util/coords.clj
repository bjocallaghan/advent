(ns net.bjoc.advent.util.coords)

(defn add-coords
  "Add multiple sets of coordinates and return a new set of coordinates.

  Usually this would be something like [x1 y1] + [x2 y2]. However, the function
  accepts an arbitrary number of coords for adding. Additionally, can handle 2D,
  3D, 4D, n-D -- so long as all coordinates to be added have equal
  dimensionality."
  [coords & more-coords]
  (let [all-coords (conj more-coords coords)]
    (when-not (apply = (map count all-coords))
      (throw (ex-info "All arguments must be vectors of the same dimension."
                      {:all-coords all-coords
                       :dimensions-present (frequencies (map count all-coords))})))
    (into [] (->> (partition (count all-coords) (apply interleave all-coords))
                  (map #(reduce + %))))))

(defn manhattan-distance
  "Return the Manhattan distance between a two points and another point (or the
  origin).

  The Manhattan distance (also known as rectilinear distance, city block
  distance, or the taxicab metric) is the sum of the absolute differences of
  their Cartesian coordinates."
  ([[x y]] (manhattan-distance [x y] [0 0]))
  ([[x1 y1] [x2 y2]]
   (+ (Math/abs (- x1 x2)) (Math/abs (- y1 y2)))))
