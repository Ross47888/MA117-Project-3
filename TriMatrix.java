/*
 * PROJECT III: TriMatrix.java
 *
 * This file contains a template for the class TriMatrix. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file. You will also need to have completed
 * the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 */

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diag;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upper;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lower;

    /**
     * Constructor function: should initialise m and n through the Matrix
     * constructor and set up the data array.
     *
     * @param N  The dimension of the array.
     */
    public TriMatrix(int N) {
        // You need to fill in this method.
        super(N,N);
        if(N < 1){
          throw new MatrixException("Matrix must have dimension > 0");
        }
        diag = new double[N];
        upper = new double[N-1];
        lower = new double[N-1];
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
        if(i >= 0 && i < m && j < m && j >= 0){
          if(i == j){
            return diag[i];
          }
          else if(i == j+1){
            return lower[j];
          }
          else if(i == j-1){
            return upper[i];
          }
          else {
            return 0;
        }
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
        if(i >= 0 && i < m && j < m && j >= 0){
          if(Math.abs(i-j) > 2){
            throw new MatrixException("TriMatrix, element must equal 0.");
          }
          else if(i == j+1){
            lower[j] = val;
          }
          else if(i == j-1){
            upper[i] = val;
          }
          else if(i == j){
            diag[i] = val;
          }
        } else {
          throw new MatrixException("Element outside dimension of array. Setting");
        }
    }


    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        TriMatrix h = this.decomp();
        double determ = 1;
        for(int i = 0; i < this.m; i++){
          determ *= h.getIJ(i,i);
        }
        return determ;
    }

    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     *
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix decomp() {
        // You need to fill in this method.
        TriMatrix temp = new TriMatrix(this.n);
        //returns the matrix as upper, mid and the lower diagonal is returned
        //as well with this matrix
        temp.setIJ(0,0,this.getIJ(0,0));
        for(int i = 0; i < this.n-1; i++){
          //sets upper diagonal
          temp.setIJ(i,i+1,this.getIJ(i,i+1));
          //sets lower diagonal
          temp.setIJ(i+1,i,this.getIJ(i+1,i) / temp.getIJ(i,i));
          //sets middle diagonal
          temp.setIJ(i+1,i+1,this.getIJ(i+1,i+1)-(temp.getIJ(i+1,i)*temp.getIJ(i,i+1)));
        }
        return temp;

    }

    /**
     * Add the matrix to another matrix A.
     *
     * @param A  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the matrix A.
     */
    public Matrix add(Matrix A){
        // You need to fill in this method.
        if(A instanceof GeneralMatrix){return A.add(this);}

        else if(A instanceof TriMatrix){
          if(A.m == this.m && A.n == this.n){
            TriMatrix temp = new TriMatrix(this.n);
            for(int i = 0; i < this.m-1; i++){
              temp.setIJ(i,i+1,this.getIJ(i,i+1)+A.getIJ(i,i+1));
              temp.setIJ(i+1,i,this.getIJ(i+1,i)+A.getIJ(i+1,i));
              temp.setIJ(i,i,this.getIJ(i,i)+A.getIJ(i,i));
            }
            temp.setIJ(A.m-1,A.m-1,this.getIJ(A.m-1,A.m-1)+A.getIJ(A.m-1,A.m-1));
            return temp;
          } else {throw new MatrixException("Dimensions must be equal");}

        } else {throw new MatrixException("Matrix type not supported.");}
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
        TriMatrix temp = new TriMatrix(this.n);
        for(int i = 0; i < this.n-1; i++){
          temp.setIJ(i,i,this.getIJ(i,i)*a);
          temp.setIJ(i,i+1,this.getIJ(i,i+1)*a);
          temp.setIJ(i+1,i,this.getIJ(i+1,i)*a);
        }
        temp.setIJ(this.n-1,this.n-1,this.getIJ(this.n-1,this.n-1)*a);
        return temp;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        for(int i = 0; i < this.n-1;i++){
          this.setIJ(i,i,Math.random());
          this.setIJ(i,i+1,Math.random());
          this.setIJ(i+1,i,Math.random());
        }
        this.setIJ(this.n-1,this.n-1,Math.random());
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // You need to fill in this method.
      TriMatrix x = new TriMatrix(3);
      x.setIJ(0,0,3);
      x.setIJ(0,1,4);
      x.setIJ(1,0,3);
      x.setIJ(1,1,2);
      x.setIJ(1,2,5);
      x.setIJ(2,1,4);
      x.setIJ(2,2,6);
      TriMatrix y = new TriMatrix(3);
      y.setIJ(0,0,2);
      y.setIJ(0,1,3);
      y.setIJ(1,0,-5);
      y.setIJ(1,1,3);
      y.setIJ(1,2,8);
      y.setIJ(2,1,2);
      y.setIJ(2,2,1);
      System.out.println(x.toString());
      for(int i = 0; i < x.m; i++){
        System.out.print(x.getIJ(0,i) + " ");
      }
      System.out.println();
      for(int i = 0; i < x.m; i++){
        System.out.print(x.getIJ(1,i) + " ");
      }
      System.out.println();
      for(int i = 0; i < x.m; i++){
        System.out.print(x.getIJ(2,i) + " ");
      }
      System.out.println();
      try{
        x.setIJ(4,4,5);
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
      try{
        x.getIJ(4,4);
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
      try{
        x.setIJ(-1,4,5);
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
      try{
        x.getIJ(-1,4);
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
      TriMatrix u = new TriMatrix(4);
      u.setIJ(0,0,3);
      u.setIJ(0,1,2);
      u.setIJ(1,0,3);
      u.setIJ(1,1,1);
      u.setIJ(1,2,5);
      u.setIJ(2,1,8);
      u.setIJ(2,2,6);
      u.setIJ(3,2,4);
      u.setIJ(2,3,6);
      u.setIJ(3,3,10);
      // System.out.println(x.decomp().toString());
      // System.out.println(y.toString());
      // System.out.println(y.decomp().toString());
      // System.out.print("\n \n");
      // System.out.println(x.toString() + "\n" + y.toString());
      // System.out.println(x.add(y) + "\n" + y.add(x));

      System.out.println(u.toString());
      try{
        System.out.println(u.multiply(x).toString());
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
      System.out.println(u.multiply(u).toString());
      System.out.println(u.multiply(u).toString());
      System.out.println(u.decomp().toString());

      System.out.println(x.toString() + "\n" + x.determinant() + "\n");
      System.out.println(y.toString() + "\n" + y.determinant() + "\n");
      System.out.println(u.toString() + "\n" + u.determinant() + "\n");
      Matrix test = new TriMatrix(5);
      TriMatrix id = new TriMatrix(3);
      for(int i = 0; i < 3;i++){
        id.setIJ(i,i,1);
      }
      // for(int i = 0; i < 10; i++){
      //   System.out.println(id.multiply(i).toString());
      // }
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
      try{
        System.out.println(x.multiply(z).toString() + "tick");
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }try{
        System.out.println(z.multiply(x).toString() + "x");
      } catch (MatrixException e){
        System.out.println(e.getMessage());
      }
    }
}
