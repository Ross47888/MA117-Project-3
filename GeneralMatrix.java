/*
 * PROJECT III: GeneralMatrix.java
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
 * are implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file. You will also need to have completed
 * the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

import java.util.Arrays;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] data;

    /**
     * Constructor function: should initialise m and n through the Matrix
     * constructor and set up the data array.
     *
     * @param m  The first dimension of the array.
     * @param n  The second dimension of the array.
     */
    public GeneralMatrix(int m, int n) throws MatrixException {
        // You need to fill in this method.
        super(m,n);

        if (m < 1 || n < 1){
          throw new MatrixException("The rows or columns of matrix cannot be of size less than 1.");
        }

        data = new double[m][n];
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the matrix A.
     *
     * @param A  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix A) {
        // You need to fill in this method.
        super(A.m,A.n);
        data = new double[A.m][A.n];
        for(int i = 0; i < m; i++){
          for(int j = 0; j < n; j++){
            this.data[i][j] = A.getIJ(i,j);
          }
        }
    }

    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        // You need to fill in this method.
        if(i >= 0 && i < this.m &&  j < this.n && j >= 0){
          return this.data[i][j];
        } else {
          throw new MatrixException("Element outside dimension of array. Getting");
        }
    }

    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i    The location in the first co-ordinate.
     * @param j    The location in the second co-ordinate.
     * @param val  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double val) {
        // You need to fill in this method.
        if(i >= 0 && i < this.m &&  j < this.n && j >= 0){
          this.data[i][j] = val;
        } else {
          throw new MatrixException("Element cannot be set outside of dimension of matrix. Setting");
        }
    }

    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        double[] d = new double[1];
        GeneralMatrix h = this.decomp(d);
        double determ = 1;
        for(int i = 0; i < this.m; i++){
          determ *= h.getIJ(i,i);
        }
        determ *= d[0];
        return determ;
    }

    /**
     * Add the matrix to another matrix A.
     *
     * @param A  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the matrix A.
     */
    public Matrix add(Matrix A) {
        // You need to fill in this method.
      if(A.m == this.m && this.n == A.n){
        GeneralMatrix temp = new GeneralMatrix(this.m,A.n);
        for(int i = 0; i < this.m; i++){
          for(int j = 0; j < this.n; j++){
            temp.setIJ(i,j,this.getIJ(i,j)+A.getIJ(i,j));
          }
        }
        return temp;
      } else{
        throw new MatrixException("Dimensions are not equal");
      }
      }

    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {
        // You need to fill in this method.
        if(A.m == this.n){
          double temp = 0;
          GeneralMatrix B = new GeneralMatrix(this.m,A.n);
          for(int i = 0; i < A.n; i++){          //columns
            for(int j = 0; j < this.m; j++){     //rows
              for(int x = 0; x < this.n; x++){
                temp += this.getIJ(j,x) * A.getIJ(x,i);
              }
              B.setIJ(j,i,temp);
              temp = 0;
            }
          }
         return B;
        } else {
          throw new MatrixException("Rows must be equal to columns");
        }

    }

    /**
     * Multiply the matrix by a scalar.
     *
     * @param a  The scalar to multiply the matrix by.
     * @return   The product of this matrix with the scalar a.
     */
    public Matrix multiply(double a) {
        // You need to fill in this method.
        GeneralMatrix temp = new GeneralMatrix(this.m,this.n);
        for(int i = 0; i < this.m; i++){
          for(int j = 0; j < this.n; j++){
            temp.setIJ(i,j,a*this.getIJ(i,j));
          }
        }
        return temp;
    }


    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        for(int i = 0; i < this.m; i ++){
          for(int j = 0; j < this.n; j++){
            this.setIJ(i,j,Math.random());
          }
        }
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     *
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 l_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * d[0] calculated by the function.
     *
     * If the matrix is singular, then the routine throws a MatrixException.
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     *
     * @param d  An array of length 1. On exit, the value contained in here
     *           will either be 1 or -1, which you can use to calculate the
     *           correct sign on the determinant.
     * @return   The LU decomposition of the matrix.
     */
    public GeneralMatrix decomp(double[] d) {
        // This method is complete. You should not even attempt to change it!!
        if (n != m)
            throw new MatrixException("Matrix is not square");
        if (d.length != 1)
            throw new MatrixException("d should be of length 1");

        int           i, imax = -10, j, k;
        double        big, dum, sum, temp;
        double[]      vv   = new double[n];
        GeneralMatrix a    = new GeneralMatrix(this);

        d[0] = 1.0;

        for (i = 1; i <= n; i++) {
            big = 0.0;
            for (j = 1; j <= n; j++)
                if ((temp = Math.abs(a.data[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }

        for (j = 1; j <= n; j++) {
            for (i = 1; i < j; i++) {
                sum = a.data[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.data[i-1][k-1]*a.data[k-1][j-1];
                a.data[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= n; i++) {
                sum = a.data[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.data[i-1][k-1]*a.data[k-1][j-1];
                a.data[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= n; k++) {
                    dum = a.data[imax-1][k-1];
                    a.data[imax-1][k-1] = a.data[j-1][k-1];
                    a.data[j-1][k-1] = dum;
                }
                d[0] = -d[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.data[j-1][j-1] == 0.0)
                a.data[j-1][j-1] = 1.0e-20;
            if (j != n) {
                dum = 1.0/a.data[j-1][j-1];
                for (i = j+1; i <= n; i++)
                    a.data[i-1][j-1] *= dum;
            }
        }

        return a;
    }

    public GeneralMatrix copy(TriMatrix alan){
      GeneralMatrix temp = new GeneralMatrix(alan.n,alan.n);
      for(int i = 0; i < alan.n; i++){
        for(int j = 0; j < alan.n; j++){
          temp.setIJ(i,j,alan.getIJ(i,j));
        }
      }
      return temp;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // You need to fill in this method.
        GeneralMatrix test = new GeneralMatrix(1,1);
        test.setIJ(0,0,1.);
        System.out.println(test.toString());
        test.setIJ(0,0,1.1);
        System.out.println(test.toString());
        test.setIJ(0,0,1.11);
        System.out.println(test.toString());
        test.setIJ(0,0,1.111);
        System.out.println(test.toString());
        test.setIJ(0,0,11.111);
        System.out.println(test.toString());
        test.setIJ(0,0,111.111);
        System.out.println(test.toString());
        test.setIJ(0,0,1111.111);
        System.out.println(test.toString());
        test.setIJ(0,0,11111.111);
        System.out.println(test.toString());
        test.setIJ(0,0,111111.111);
        System.out.println(test.toString());
        test.setIJ(0,0,1111111.111);
        System.out.println(test.toString() + "7");
        GeneralMatrix z = new GeneralMatrix(3,4);
        z.setIJ(0,0,3);
        z.setIJ(0,1,4);
        z.setIJ(1,0,5);
        z.setIJ(1,1,4);
        z.setIJ(1,2,2);
        z.setIJ(2,1,4);
        z.setIJ(2,2,6);;
        z.setIJ(2,3,4);
        z.setIJ(1,3,1);
        z.setIJ(0,3,2);
        GeneralMatrix bob = new GeneralMatrix(z);
        System.out.println(bob.toString());
        try{
          bob.setIJ(5,5,10);
        } catch (MatrixException e){
          System.out.println(e.getMessage());
        }
        try{
          System.out.println(bob.getIJ(5,5));
        } catch (MatrixException e){
          System.out.println(e.getMessage());
        }
        Matrix bobb;
        for(int i = 4; i < 10; i++){
          bobb = new GeneralMatrix(i,i);
          bobb.random();
          bobb = bobb.multiply(10);
          System.out.println(bobb.toString() + bobb.determinant() + "\n");
          bobb.random();
          bobb = bobb.multiply(1000);
          System.out.println(bobb.toString() + bobb.determinant() + "\n");
          bobb.random();
          System.out.println(bobb.toString() + bobb.determinant() + "\n");
        }
        GeneralMatrix topDog = new GeneralMatrix(3,4);
        topDog.random();
        topDog.setIJ(0,0,10000);
        System.out.println("hello\n" + topDog.toString());



  }
}
