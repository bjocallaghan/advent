(ns net.bjoc.advent.util.matrix-visualization
  (:import [java.awt Color Graphics Dimension]
           [java.awt.image BufferedImage]
           [javax.swing JPanel JFrame]
           [javax.imageio ImageIO])
  (:require [clojure.string :as str]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn- paint-color-value
  [#^Graphics g x-min y-min width height color]
    (doto g
      (.setColor color)
      (.fillRect x-min y-min width height)))

(defn- palletted-paint-char-value
  [pallette g x-min y-min width height value]
  (when-let [c (pallette value)]
    (paint-color-value g x-min y-min width height c)))

(defn- rand-color []
  (. Color getHSBColor (rand) (+ 0.25 (rand 0.5)) (+ 0.25 (rand 0.5))))

(defn- generate-color-seq []
  (let [infinite-rand-colors
        (fn rand-color-seq []
          (lazy-seq (cons (rand-color) (rand-color-seq))))]
    (concat (list 
             (. Color RED)
             (. Color BLUE)
             (. Color YELLOW)
             (. Color PINK)
             (. Color CYAN)
             (. Color GREEN)
             (. Color ORANGE)
             (. Color MAGENTA))
            (infinite-rand-colors))))

(defn- generate-pallette
  "Generate a semi-random map of colors for the values present in a matrix."
  [matrix]
  (into {} (map vector (-> matrix vals set) (generate-color-seq))))

(defn- guess-paint-value-fn
  "Attempts to guess an appropriate function for filling the element cells based
  on the provided options or the values found inside matrix."
  [matrix
   & {:keys [pallette type]
      :as options}]
  (cond
    pallette (partial palletted-paint-char-value pallette)
    :else
    (case (or type (mtx/guess-type matrix))
      :colors paint-color-value
      :characters (partial palletted-paint-char-value (generate-pallette matrix))
      :keywords (partial palletted-paint-char-value (generate-pallette matrix))
      )))

(defn image
  "Return a bitmap image of a matrix."
  [matrix
   & {:keys [paint-value-fn img-size background-color value-repr-fn]
      :or {img-size [500 500]
           background-color (. Color white)
           value-repr-fn identity}
      :as options}]
  (let [matrix (reduce-kv (fn [m k v] (assoc m k (value-repr-fn v))) {} matrix)
        paint-value-fn (or paint-value-fn
                           (apply (partial guess-paint-value-fn matrix)
                                  (mapcat identity options)))
        [num-cols num-rows] (mtx/size matrix)
        scale (/ (double (apply max img-size)) (apply max (mtx/size matrix)))
        px-x-size (Math/round (* scale num-cols))
        px-y-size (Math/round (* scale num-rows))
        x-intervals (map #(Math/round (* scale %)) (range (inc num-cols)))
        y-intervals (map #(Math/round (* scale %)) (range (inc num-rows)))
        specs-map (into {}
                        (for [x (range num-cols)
                              y (range num-rows)]
                          [[x y]
                           {:x (nth x-intervals x)
                            :y (nth y-intervals y)
                            :width (- (nth x-intervals (inc x)) (nth x-intervals x))
                            :height (- (nth y-intervals (inc y)) (nth y-intervals y))}]))
        img (new BufferedImage px-x-size px-y-size
                 (. BufferedImage TYPE_INT_ARGB))
        g (. img (getGraphics))]
    (doto g
      (.setColor background-color)
      (.fillRect 0 0 px-x-size px-y-size))
    (dorun
     (for [[xy v] matrix]
       (let [{:keys [x y width height]} (specs-map xy)]
         (paint-value-fn g x y width height v))))
    img))

(defn write-image-file
  "Save an image of a matrix to a file."
  [matrix dest-filename & {:as options}]
  (when-not (re-find #"\.png$" dest-filename)
    (throw (ex-info "Destination filename must end in '.png'"
                    {:filename dest-filename})))
  (let [img (apply (partial image matrix)
                   (mapcat identity options))]
    (. ImageIO write img "png" (java.io.File. dest-filename))
    img))

(defn- as-xy [dimension]
  [(int (.getWidth dimension)) (int (.getHeight dimension))])

(defn show
  "Display an image of a matrix in a new window.

  Although the simple process is: generate the image of the matrix and slap it
  on a paneled frame, for performance reasons we only want to figure out the
  'paint-value-fn' stuff once. Therefore the implementation has some [hard to
  understand?] pre-computation stuff.

  Additionally, the implementation is further complicated by code to keep the
  image centered in the frame."
  [matrix
   & {:keys [window-size paint-value-fn value-repr-fn]
      :or {window-size 800
           value-repr-fn identity}
      :as options}]
  (let [matrix (reduce-kv (fn [m k v] (assoc m k (value-repr-fn v))) {} matrix)
        paint-value-fn (or paint-value-fn
                           (apply (partial guess-paint-value-fn matrix)
                                  (mapcat identity options)))
        img-fn #(apply (partial image matrix)
                       (mapcat identity (-> options
                                            (dissoc :value-repr-fn)
                                            (assoc :paint-value-fn paint-value-fn)
                                            (assoc :img-size %))))
        paint-panel-fn (fn [panel #^Graphics g]
                         (let [[x y] (as-xy (.getSize panel))
                               lim (min x y)
                               x-offset (-> x (- lim) (quot 2))
                               y-offset (-> y (- lim) (quot 2))
                               img (img-fn [lim lim])]
                           (. g (drawImage img x-offset y-offset nil))
                           (.dispose #^Graphics (. img (getGraphics)))))
        panel (doto (proxy [JPanel] []
                      (paint [g]
                        (paint-panel-fn this g)))
                (.setPreferredSize (new Dimension window-size window-size)))
        frame (new JFrame)]
    (doto frame (.add panel) .pack .show)
    [frame panel]))
