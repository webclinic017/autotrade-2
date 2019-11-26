package dao;

import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import dao.ConnectionUtils;
//import entity.Area;
//import entity.Scenario;
import entity.OrderSign;
//import entity.Volume;
import entity.Zone;
import systemenum.SystemEnum;
import systemenum.SystemEnum.OrderAction;
import tool.Util;

public class CommonDAOSQL implements CommonDAO {


	@Override
	public ArrayList<OrderSign> getOrderSignListByDate(Date date, String setting) {
		final String sqlString = "select * from order_sign where date = ? and setting = ?";
		ArrayList<OrderSign> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1, Util.getDateStringByDateAndFormatter(date, "yyyy/MM/dd"));
			stmt.setString(2, setting);
			rs = stmt.executeQuery();
	        while(rs.next()){

	        	StringBuilder dateStr = new StringBuilder(rs.getString(1));
	        	dateStr.append(" ");
	        	dateStr.append(rs.getString(2));
	        	OrderSign sign = new OrderSign();
	        	sign.setTime(Util.getDateByStringAndFormatter(dateStr.toString(), "yyyy/MM/dd HH:mm:ss"));
	        	sign.setSetting(rs.getString(3));
	        	sign.setActionText(rs.getString(4));
	        	sign.setOrderAction(Util.getOrderActionEnumByText(sign.getActionText()));
	        	sign.setOrderPrice(rs.getDouble(5));
	        	sign.setTick(rs.getDouble(6));
	        	sign.setLimitPrice(rs.getDouble(7));
	        	sign.setClosePrice(rs.getDouble(8));
	        	sign.setTickProfit(rs.getDouble(9));
	        	
	        	list.add(sign);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}

	@Override
	public Enum<SystemEnum.OrderAction> getLastActionBySetting(Date date, String setting) {
		final String sqlString = "select action from order_sign where date = ? and setting = ? order by time desc";
		
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1, Util.getDateStringByDateAndFormatter(date, "yyyy/MM/dd"));
			stmt.setString(2, setting);
			rs = stmt.executeQuery();
	        if(rs.next()){

	        	String actionTextString = rs.getString(1);
	        	return Util.getOrderActionEnumByText(actionTextString);
	        }
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		
		return SystemEnum.OrderAction.Default;
	}

	@Override
	public void insertNewOrderSign(OrderSign sign) {
		final String sqlString = "insert into order_sign (date,time,setting,action,limit_price,tick,stop_price,tick_profit) values (?,?,?,?,?,?,?,?)";

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1, Util.getDateStringByDateAndFormatter(sign.getTime(),"yyyy/MM/dd"));
			stmt.setString(2, Util.getDateStringByDateAndFormatter(sign.getTime(),"HH:mm:ss"));
			stmt.setString(3, sign.getSetting());
			stmt.setString(4, sign.getActionText());
			stmt.setDouble(5, sign.getLimitPrice());
			stmt.setDouble(6, sign.getTick());
			stmt.setDouble(7, sign.getStopPrice());
			stmt.setDouble(8, sign.getTickProfit());
			int i = stmt.executeUpdate();
	        if (i == 0) {
				//False
	        	System.out.print("insertNewTrendSign fail");
			}
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
	}
	

	@Override
	public void updateOrderInfo(String setting, String time, double limitPrice, double closePrice, double tickProfit) {
		final String sqlString = "update order_sign set limit_price = ?, close_price = ?, tick_profit = ? where setting = ? and time = ? and date = ?";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setDouble(1,limitPrice);
			stmt.setDouble(2,closePrice);
			stmt.setDouble(3,tickProfit);
			stmt.setString(4,setting);
			stmt.setString(5,time);
			stmt.setString(6,Util.getDateStringByDateAndFormatter(new Date(), "yyyy/MM/dd"));
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
		
	}
	/*
	@Override
	public ArrayList<String>getAllActiveScenarioName()  {
		
		final String sqlString = "select scenario from scenario_active where active = ?";
		ArrayList<String> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setInt(1, 1);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	list.add(rs.getString(1));
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getAllDistinctScenarioStartTimeAndEndTime() {
		final String sqlString = "SELECT start_time FROM scenario UNION SELECT end_time FROM scenario";
		ArrayList<String> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	list.add(rs.getString(1));
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getAllDistinctVolumeStartTimeAndEndTime() {
		final String sqlString = "SELECT start_time FROM volume UNION SELECT end_time FROM volume";
		ArrayList<String> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	list.add(rs.getString(1));
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<Scenario> getAllWorkingScenarioAtTime(Date time) {
		
		final String sqlString = "select distinct(scenario) from scenario where start_time <= ? and end_time > ?";
		ArrayList<Scenario> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
	    	String timeStr = tool.Util.getDateStringByDateAndFormatter(time, "HH:mm:ss");
	    	
			stmt.setString(1, timeStr);
			stmt.setString(2, timeStr);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	Scenario scenario = new Scenario();
	        	scenario.setScenario(rs.getString(1));
	        	list.add(scenario);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<Area> getAreaListWithoutZoneByScenario(String scenario,Date time) {
		final String sqlString = "select start_time,end_time,area,percent,white_min from scenario where scenario = ? and start_time <= ? and end_time > ?";
		ArrayList<Area> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			String timeStr = tool.Util.getDateStringByDateAndFormatter(time, "HH:mm:ss");
			stmt.setString(1, scenario);
			stmt.setString(2, timeStr);
			stmt.setString(3, timeStr);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	Area area = new Area();
	        	area.setStartTime(rs.getString(1));
	        	area.setEndTime(rs.getString(2));
	        	area.setArea(rs.getString(3));
	        	area.setPercent(rs.getInt(4));
	        	area.setWhiteMin(rs.getInt(5));
	        	area.setScenario(scenario);
	        	list.add(area);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getOnlyActiveZoneListByScenarioArea(String scenario, String startTime, String area) {
		final String sqlString = "select zone from area_zone where scenario = ? and start_time = ? and area = ? and active = 1";
		ArrayList<String> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1, scenario);
			stmt.setString(2, startTime);
			stmt.setString(3, area);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	list.add(rs.getString(1));
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	
	@Override
	public ArrayList<Zone> getRelatedZoneListByScenarioList(ArrayList<Scenario> scenarioList) {
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT distinct(zone) FROM area_zone where ");
		for (int i = 0; i < scenarioList.size(); i++) {
			
			Scenario s = scenarioList.get(i);
			
			for (int j = 0; j < s.getAreaList().size(); j++) {
				
				Area a = s.getAreaList().get(j);
				
				sBuilder.append("(scenario = '" + a.getScenario() + "' and start_time = '" + a.getStartTime() + "' and area = '" + a.getArea() + "' and active = 1)");
				if(j + 1 < s.getAreaList().size()) {
					sBuilder.append(" or ");
				}
			}
			if(i + 1 < scenarioList.size()) {
				sBuilder.append(" or ");
			}
		}
		
		ArrayList<Zone> list = new ArrayList<>();
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sBuilder.toString());
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	Zone zone =  new Zone();
	        	zone.setZone(rs.getString(1));
	        	Util.setZoneXYByZone(zone);
	        	list.add(zone);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}

	@Override
	public ArrayList<Zone> getVolumeZoneList(String time) {

		final String sqlString = "select zone from volume_zone where time = ?";

		ArrayList<Zone> list = new ArrayList<>();
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,time);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	Zone zone =  new Zone();
	        	zone.setZone(rs.getString(1));
	        	Util.setZoneXYByZone(zone);
	        	list.add(zone);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}

	@Override
	public ArrayList<Volume> getAllWorkingVolumeAtTime(Date time) {
		final String sqlString = "select scenario,column,percent,white_max,rows from volume where start_time <= ? and end_time > ?";
		ArrayList<Volume> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
	    	String timeStr = tool.Util.getDateStringByDateAndFormatter(time, "HH:mm:ss");
	    	
			stmt.setString(1, timeStr);
			stmt.setString(2, timeStr);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	Volume volume = new Volume();
	        	volume.setScenario(rs.getString(1));
	        	volume.setColumn(rs.getInt(2));
	        	volume.setPercent(rs.getInt(3));
	        	volume.setWhiteMax(rs.getInt(4));
	        	String rows = rs.getString(5);
	        	String[] rowList = rows.split("_");
	        	for (String row : rowList) { 
	        		volume.getRows().add(row);
	        	}
	        	
	        	list.add(volume);
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}

	@Override
	public Rectangle getRectByName(String name) {
		final String sqlString = "select origin_x,origin_y,width,height from my_frame where name = ?";
		Rectangle rect = new Rectangle();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
	        if(rs.next()){
	        	
	        	rect.setBounds(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
	        }
			return rect;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return rect;
	}

	
	@Override
	public void cleanScenarioActiveData() {
		final String sqlString = "delete from scenario_active";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void cleanScenarioData() {
		final String sqlString = "delete from scenario";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}	
	}
	
	@Override
	public void cleanAreaZone() {
		final String sqlString = "delete from area_zone";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void cleanMyFrame() {
		final String sqlString = "delete from my_frame";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
		
	}

	@Override
	public void cleanVolumeZone() {
		final String sqlString = "delete from volume_zone";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void cleanVolume() {
		final String sqlString = "delete from volume";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}
	
	@Override
	public void insertScenarioActive(String scenario, int active) {
		final String sqlString = "insert into scenario_active values (?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,scenario);
			stmt.setInt(2, active);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
		
	}

	@Override
	public void insertScenario(String scenario, String starttime, String endtime, String area, int percent, int whiteMin) {
		final String sqlString = "insert into scenario values (?,?,?,?,?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,scenario);
			stmt.setString(2,starttime);
			stmt.setString(3,endtime);
			stmt.setString(4,area);
			stmt.setInt(5, percent);
			stmt.setInt(6, whiteMin);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}
	
	@Override
	public void insertAreaZone(String scenario, String starttime, String area, String zone, int active) {
		final String sqlString = "insert into area_zone values (?,?,?,?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,scenario);
			stmt.setString(2,starttime);
			stmt.setString(3,area);
			stmt.setString(4,zone);
			stmt.setInt(5,active);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void insertMyFrame(String name, int x, int y, int width, int height) {
		final String sqlString = "insert into my_frame values (?,?,?,?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,name);
			stmt.setInt(2,x);
			stmt.setInt(3,y);
			stmt.setInt(4,width);
			stmt.setInt(5,height);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void insertVolumeZone(String time, String zone) {
		final String sqlString = "insert into volume_zone values (?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,time);
			stmt.setString(2,zone);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}

	@Override
	public void insertVolume(String scenario, String starttime, String endtime, int column, int percent, int whiteMax,
			String rows) {
		final String sqlString = "insert into volume values (?,?,?,?,?,?,?)";
		
		Connection conn = null;
        PreparedStatement stmt = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			stmt.setString(1,scenario);
			stmt.setString(2,starttime);
			stmt.setString(3,endtime);
			stmt.setInt(4,column);
			stmt.setInt(5,percent);
			stmt.setInt(6, whiteMax);
			stmt.setString(7,rows);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt,null);
		}
	}


	@Override
	public ArrayList<String> getAllDistinctVolumeZoneStartTime() {
		final String sqlString = "select distinct(time) from volume_zone";
		ArrayList<String> list = new ArrayList<>();

		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			
			conn = ConnectionUtils.getConnection();
			stmt = conn.prepareStatement(sqlString);
			rs = stmt.executeQuery();
	        while(rs.next()){
	        	
	        	list.add(rs.getString(1));
	        }
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
	        ConnectionUtils.closeAll(stmt, rs);
		}
		return list;
	}
	*/
}
