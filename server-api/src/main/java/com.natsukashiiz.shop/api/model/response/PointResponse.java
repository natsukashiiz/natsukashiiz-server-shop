package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.entity.Point;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PointResponse implements Serializable {
    private double point;

    public static PointResponse build(Point point) {

        PointResponse response = new PointResponse();
        response.setPoint(point.getPoint());

        return response;
    }
}
