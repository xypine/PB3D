/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

/**
 *
 * @author Jonnelafin
 */
public class gpuTools {
    
    
    
    static float[][] matmul(float[][] a, float[][] b) {
        int colsA = a[0].length;
        int rowsA = a.length;
        int colsB = b[0].length;
        int rowsB = b.length;

        if (colsA != rowsB) {
          throw new Error("Columns of A must match rows of B");
        }

        float result[][] = new float[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
          for (int j = 0; j < colsB; j++) {
            float sum = 0;
            for (int k = 0; k < colsA; k++) {
              sum += a[i][k] * b[k][j];
            }
            result[i][j] = sum;
          }
        }
        return result;
    }
}
