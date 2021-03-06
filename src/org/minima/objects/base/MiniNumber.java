/**
 * 
 */
package org.minima.objects.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.minima.utils.Streamable;

/**
 * @author Spartacus Rex
 *
 */
public class MiniNumber implements Streamable {
	
	/**
	 * The Math Context used for ALL real numbers
	 * 
	 * Can represent 1 billion with 8 zeros ( 10 + 8 ) digits with no loss of precision.. 
	 * 
	 * But all Minima values are actually in significant digit format anyway.. so semi-infinite precision..
	 */
	public static final MathContext mMathContext = new MathContext(18, RoundingMode.DOWN);
	
	/**
	 * The decimal precision of the significant digits.
	 */
	public static final DecimalFormat MINIMA_SIGNIFICANT_FORMAT = new DecimalFormat("0.#################E0");
	
	/**
	 * Useful numbers
	 */
	public static final MiniNumber ZERO 		= new MiniNumber("0");
	public static final MiniNumber ONE 		    = new MiniNumber("1");
	public static final MiniNumber TWO 		    = new MiniNumber("2");
	public static final MiniNumber EIGHT        = new MiniNumber("8");
	
	public static final MiniNumber MINUSONE 	= new MiniNumber("-1");
	
	/**
	 * The number representation
	 */
	private BigDecimal mNumber;
	
	/**
	 * 
	 */
	public MiniNumber(){
		mNumber = BigDecimal.ZERO;
	}
	
//	public MiniNumber(int zNumber){
//		mNumber = new BigDecimal(zNumber,mMathContext);
//	}

//	public MiniNumber(long zNumber){
//		mNumber = new BigDecimal(zNumber,mMathContext);
//	}
	
//	public MiniNumber(double zNumber){
//		mNumber = new BigDecimal(zNumber,mMathContext);
//	}
	
	public MiniNumber(BigInteger zNumber){
		mNumber = new BigDecimal(zNumber,mMathContext);
	}
	
	public MiniNumber(BigDecimal zBigD){
		mNumber = zBigD;
	}
	
	public MiniNumber(String zNumber){
		mNumber = new BigDecimal(zNumber,mMathContext);
	}
	
	public BigDecimal getAsBigDecimal() {
		return mNumber;
	}
	
	public BigInteger getAsBigInteger() {
		return mNumber.toBigInteger();
	}
	
	public double getAsDouble() {
		return mNumber.doubleValue();
	}
	
	public long getAsLong() {
		return mNumber.longValue();
	}
	
	public int getAsInt() {
		return mNumber.intValue();
	}
	
	public MiniNumber add(MiniNumber zNumber) {
		return new MiniNumber( mNumber.add(zNumber.getAsBigDecimal(),mMathContext) );
	}
	
	public MiniNumber sub(MiniNumber zNumber) {
		return new MiniNumber( mNumber.subtract(zNumber.getAsBigDecimal(),mMathContext) );
	}
	
	public MiniNumber div(MiniNumber zNumber) {
		return new MiniNumber( mNumber.divide(zNumber.getAsBigDecimal(), mMathContext) );
	}
	
	public MiniNumber divRoundDown(MiniNumber zNumber) {
		return new MiniNumber( mNumber.divide(zNumber.getAsBigDecimal(), RoundingMode.DOWN) );
	}
	
	public MiniNumber mult(MiniNumber zNumber) {
		return new MiniNumber( mNumber.multiply(zNumber.getAsBigDecimal(),mMathContext) );
	}
	
	public MiniNumber modulo(MiniNumber zNumber) {
		return new MiniNumber( mNumber.remainder(zNumber.getAsBigDecimal(),mMathContext) );
	}
	
	public MiniNumber floor() {
		return new MiniNumber( mNumber.setScale(0, RoundingMode.FLOOR) ) ;
	}
	
	public MiniNumber ceil() {
		return new MiniNumber( mNumber.setScale(0, RoundingMode.CEILING) ) ;
	}
	
	public MiniNumber abs() {
		return new MiniNumber( mNumber.abs() ) ;
	}
		
	public MiniNumber increment() {
		return new MiniNumber( mNumber.add(BigDecimal.ONE,mMathContext) );
	}
	
	public MiniNumber decrement() {
		return new MiniNumber( mNumber.subtract(BigDecimal.ONE,mMathContext) );
	}

	public int compareTo(MiniNumber zCompare) {
		return mNumber.compareTo(zCompare.getAsBigDecimal());
	}
	
	public boolean isEqual(MiniNumber zNumber) {
		return compareTo(zNumber)==0;
	}
	
	public boolean isLess(MiniNumber zNumber) {
		return compareTo(zNumber)<0;
	}
	
	public boolean isLessEqual(MiniNumber zNumber) {
		return compareTo(zNumber)<=0;
	}
	
	public boolean isMore(MiniNumber zNumber) {
		return compareTo(zNumber)>0;
	}
	
	public boolean isMoreEqual(MiniNumber zNumber) {
		return compareTo(zNumber)>=0;
	}
		
	@Override
	public String toString(){
		return mNumber.toPlainString();
	}

	/**
	 * Output the scale and unscaled value..
	 */
	@Override
	public void writeDataStream(DataOutputStream zOut) throws IOException {
		//Write out the scale..
		zOut.writeInt(mNumber.scale());
		
		//And now the unscaled value..
		byte[] data = mNumber.unscaledValue().toByteArray();
		int len = data.length;
		
		zOut.writeInt(len);
		zOut.write(data);
		
		//Simples
//		zOut.writeUTF(toString());
	}

	@Override
	public void readDataStream(DataInputStream zIn) throws IOException {
		//Read in the scale
		int scale = zIn.readInt();
		
		//Read in the byte array for unscaled BigInteger
		int len = zIn.readInt();
		byte[] data = new byte[len];
		zIn.readFully(data);
		
		//And create..
		BigInteger unscaled = new BigInteger(data);
		mNumber = new BigDecimal(unscaled,scale,mMathContext);
		
		//Simples
//		mNumber = new BigDecimal(zIn.readUTF(),mMathContext);
	}

	public static MiniNumber ReadFromStream(DataInputStream zIn){
		MiniNumber data = new MiniNumber();
		
		try {
			data.readDataStream(zIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
	
	public static void main(String[] zArgs) {
		
		MiniNumber num = new MiniNumber("10000.001");
		
		System.out.println(num);
		
	}
}
