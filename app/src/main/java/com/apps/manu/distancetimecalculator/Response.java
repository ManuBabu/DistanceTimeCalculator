package com.apps.manu.distancetimecalculator;

import java.util.List;

/**
 * Created by Admin on 12/18/2015.
 */
public class Response {

    /**
     * destination_addresses : ["Suranjan Das Rd, Puttappa layout, New Tippasandra, Bengaluru, Karnataka 560017, India"]
     * origin_addresses : ["1, Victoria Rd, Officers Colony, Victoria Layout, Bengaluru, Karnataka 560047, India"]
     * rows : [{"elements":[{"distance":{"text":"8.8 km","value":8759},"duration":{"text":"22 mins","value":1328},"status":"OK"}]}]
     * status : OK
     */

    private String status;
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowsEntity> rows;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * distance : {"text":"8.8 km","value":8759}
         * duration : {"text":"22 mins","value":1328}
         * status : OK
         */

        private List<ElementsEntity> elements;

        public void setElements(List<ElementsEntity> elements) {
            this.elements = elements;
        }

        public List<ElementsEntity> getElements() {
            return elements;
        }

        public static class ElementsEntity {
            /**
             * text : 8.8 km
             * value : 8759
             */

            private DistanceEntity distance;
            /**
             * text : 22 mins
             * value : 1328
             */

            private DurationEntity duration;
            private String status;

            public void setDistance(DistanceEntity distance) {
                this.distance = distance;
            }

            public void setDuration(DurationEntity duration) {
                this.duration = duration;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public DistanceEntity getDistance() {
                return distance;
            }

            public DurationEntity getDuration() {
                return duration;
            }

            public String getStatus() {
                return status;
            }

            public static class DistanceEntity {
                private String text;
                private int value;

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public static class DurationEntity {
                private String text;
                private int value;

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }
        }
    }
}
