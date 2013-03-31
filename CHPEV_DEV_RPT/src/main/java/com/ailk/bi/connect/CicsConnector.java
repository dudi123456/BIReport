package com.ailk.bi.connect;

//import com.ibm.ctg.client.*;

class CicsConnector {// extends AbstractConnector{
/*
 * CicsConnector(){ }
 * 
 * public DataPackage callService(String serverName,String serviceName) throws
 * ConnectException { // //开发模式调用callService0不与交易中间件交互 // if (isDevelopMode())
 * return callService0(serverName,serviceName);
 * 
 * String strJGateName = "localhost"; // JGate name String strServerName =
 * "R600"; // CICS server name String strPassword = ""; // CICS password String
 * strUserId = "CICSUSER"; // CICS userid
 * 
 * //压缩、生成发送数据 DataPackage pkgIn = getInDataBuffer(); byte[] byteBuf =
 * pkgIn.DumpIntoString().getBytes(); byte[] commAreaIn = new
 * byte[ControlEntry.size() + byteBuf.length]; int length = compress(byteBuf, 0,
 * commAreaIn, ControlEntry.size(), byteBuf.length); ControlEntry controlEntry =
 * new ControlEntry(); controlEntry.setCompressLength(length);
 * System.arraycopy(controlEntry.format().getBytes(), 0, commAreaIn, 0,
 * ControlEntry.size()); //调用CICS交易 JavaGateway jgaConnection; ECIRequest
 * eciRequest = null; try{ jgaConnection = new JavaGateway();
 * jgaConnection.setURL(strJGateName); jgaConnection.open(); eciRequest = new
 * ECIRequest("", // CICS Server strUserId, // UserId, null for none
 * strPassword, // Password, null for none "", // Program name commAreaIn, //
 * Commarea ECIRequest.ECI_NO_EXTEND, ECIRequest.ECI_LUW_NEW);
 * eciRequest.Program = serviceName; jgaConnection.flow(eciRequest); } catch
 * (Exception e) { throw new ConnectException(); }finally{ if (jgaConnection !=
 * null) { jgaConnection.close(); } }
 * 
 * //解压缩、返回数据 controlEntry = ControlEntry.parse(new String(eciRequest.Commarea,
 * 0, ControlEntry.size() ) ); byteBuf = new
 * byte[controlEntry.getCompressLength()];
 * 
 * uncompress(eciRequest.Commarea, ControlEntry.size(), byteBuf, 0,
 * byteBuf.length );
 * 
 * DataPackage pkgOut = getOutDataBuffer(); pkgOut.clear().StringIntoPkg(new
 * String(byteBuf));
 * 
 * return pkgOut; }
 */

}