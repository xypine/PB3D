/*
 * The MIT License
 *
 * Copyright 2020 Elias Eskelinen <elias.eskelinen@protonmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package codenameprojection.renderer;

import JFUtils.point.Point2D;
import codenameprojection.drawables.vertexGroup;
import codenameprojection.driver;
import codenameprojection.drawables.face;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Elias Eskelinen aka Jonnelafin
 */
public interface rendererInterface {

    HashMap getIDMap();

    void updateFaces(LinkedList<face> newSet, LinkedList<Color> color, LinkedList<Float> dist);

    void updateLines(LinkedList<Integer[]> newSet, LinkedList<Color> color);

    void updatePoints(LinkedList<Point2D> newSet, LinkedList<Point2D> newSizes, LinkedList<Integer> pointsToHide, LinkedList<vertexGroup> color, driver Logic);
    
}
