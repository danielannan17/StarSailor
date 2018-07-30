/***
 * Class to represent 2d vectors and operations on them.
 *
 */
package entities;

public class Vector2D {
	private float x, y;
	
	/**
	 * create a new vector
	 * @param x the x value of the vector
	 * @param y the y value of the vector
	 */
	public Vector2D(float x,float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * create a new vector
	 * @param direction the direction in which the vector should be (radians)
	 * @param magnitude the magnitude of the vector
	 */
	public Vector2D(double direction, float magnitude) {
		x = (float) Math.cos(direction);
		y = (float) Math.sin(direction);
		this.setMagnitude(magnitude);
	}
	
	/**
	 * create a new vector with no direction or magnitude
	 */
	public Vector2D(){
		x = 0;
		y = 0;
	}
	
	/**
	 * return a copy of this vector
	 * @return
	 */
	public Vector2D copy() {
		return new Vector2D(x,y);
	}
	
	/**
	 * get the x value of the vector
	 * @return the x value
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * get the y value of the vector
	 * @return the y value
	 */
	public float getY(){
		return y;
	}
	
	/**
	 * convert 2 points to the angle between them
	 * @param x1 the x coord of point 1
	 * @param y1 the y coord of point 1
	 * @param x2 the x coord of point 2
	 * @param y2 the y coord of point 2
	 * @return
	 */
	public static double convertToDirection(int x1, int y1, int x2, int y2) {
		float xDistance = x1 - x2;
		float yDistance = y1 - y2;
		return Math.atan2(yDistance, xDistance);
	}
	
	/**
	 * convert 2 float points to the angle between them
	 * @param x1 the x coord of point 1
	 * @param y1 the y coord of point 1
	 * @param x2 the x coord of point 2
	 * @param y2 the y coord of point 2
	 * @return
	 */
	public static double convertToDirection(float x1, float y1, float x2, float y2) {
		float xDistance = x1 - x2;
		float yDistance = y1 - y2;
		return Math.atan2(yDistance, xDistance);
	}
	
	/**
	 * get the magnitude of the vector
	 * @return the magnitude
	 */
	public float getMagnitude(){
		return (float) (Math.sqrt(Math.pow(this.getX(), 2) +  Math.pow(this.getY(),2)));
	}
	
	/**
	 * set the vector to have a length of 1
	 * @return the normalized vector
	 */
	public Vector2D normalize(){
		float oldMag = getMagnitude();
		if (oldMag == 0){
			return this;
		}
		x /= oldMag;
		y /= oldMag;
		return this;
	}
	
	/**
	 * set the magnitude of the vector
	 * @param mag the new magnitude
	 */
	public void setMagnitude(float mag){
		normalize();
		mult(mag);
	}
	
	/**
	 * multiply this vector with a scalar
	 * @param scalar the scalar to multiply by
	 * @return the new vector
	 */
	public Vector2D mult(float scalar)	{
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	/**
	 * divide this vector by a scalar
	 * @param scalar the scalar to divide by
	 * @return the new vector
	 */
	public Vector2D div(float scalar){	
		if (scalar != 0) {
			x /= scalar;
			y /= scalar;
		}
		return this;
	}
	
	/**
	 * add a vector to this vector
	 * @param vec2 the vector to add
	 * @return the new vector
	 */
	public Vector2D add(Vector2D vec2){
		if (vec2 == null) {
			System.out.println("null vector passed to add.");
			return this;
		}
		x += vec2.getX();
		y += vec2.getY();
		return this;
	}
	
	/**
	 * subtract a vector from this vector
	 * @param vec2 the vector to subtract
	 * @return the new vector
	 */
	public Vector2D sub(Vector2D vec2){
		if (vec2 == null) {
			System.out.println("null vector passed to sub.");
			return this;
		}
		x -= vec2.getX();
		y -= vec2.getY();
		return this;
	}
	
	/**
	 * return a string representation of the vector
	 */
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
