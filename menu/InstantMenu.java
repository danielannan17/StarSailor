package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import entities.AvailableSkill;
import entities.Selector;
import handlers.InputHandler;
import handlers.MathHandler;
import handlers.ResourceHandler;
import instantBattle.Battle;
import main.State;
import messaging.PopUp;
import networking.BattleModeClient;
import networking.BattleModeServer;
import networking.ServerState;

/**
 * the instant battle menu for the game
 * 
 *
 */
public class InstantMenu extends Menu {

	 static final long serialVersionUID = 1L;
	 Label labelR, labelG, labelB;
	 Slider colorR, colorG, colorB;
	 Button start, back;
	 Color playerColor = Color.white, prevColor = Color.white;
	 BufferedImage player, base, overlay;
	 int skill1, skill2,skill3;
	 AvailableSkill[] skill1Cycle, skill2Cycle, skill3Cycle; 
	 int ship;
	 BattleModeServer server;
	 boolean hosting, connected, ready;
	 int[] myFleet, enemyFleet;
	 int maxShips = 10;
	 IconLabel shipIcon, skill1Icon, skill2Icon, skill3Icon;
	 JPanel playerSelectorPanel;
	 JPanel colourPanel;
	 static BattleModeClient client;
	 Thread serverThread;
	 Thread clientThread;
	 static Image[] myShipSprites;
	Button connect;
	 Label serverIP;
	 Label serverPort;
	 Button host;
	 TextBox ip;
	 TextBox port;
	 Label status;
	 BattleButton prevskill3;
	 BattleButton nextskill3;
	 BattleButton prevskill2;
	 BattleButton nextskill2;
	 BattleButton prevSkill1;
	 BattleButton nextSkill1;
	 BattleButton prevShip;
	 BattleButton nextShip;
	 Button[] incrementButtons;
	 Button[] decrementButtons;
	 PopUp controls;
	 int[] shipStats;
	 boolean controlsClosed;
	 HoverPopUp skill3Info;
	 HoverPopUp skill2Info;
	 HoverPopUp skill1Info;
	 HoverPopUp shipInfo;
	

	public static Image[] getMyShipSprites() {
		return myShipSprites;
	}

	 void initialiseSelectors() {
		ship = 1;
		skill1 = 0;
		skill2 = 0;
		skill3 = 0;
		skill1Cycle = AvailableSkill.getSkills(true, true, false);
		skill2Cycle = AvailableSkill.getSkills(true, true, false);
		skill3Cycle = AvailableSkill.getSkills(false, false, true);
	}
	
	 void addShipSelector(int x, int y, int width, int height) {
		JPanel shipPanel = new JPanel();
		controls = new PopUp("Welcome to the instant battle. Here you can \n"
							+ "select your fleet and customise a ship and \n"
							+ "take it into battle against a friend. While in \n"
							+ "battle use WASD to move, left Click to attack \n"
							+ "and Q, E and Shift are to use your selected \n"
							+ "skills.");
		
		shipPanel.setBounds(x, y, width, height);
		shipPanel.setBackground(new Color(0,0,0,0));
		shipPanel.setLayout(new GridLayout(0, 1, 0, 0));
		nextShip = new BattleButton(1, 0, 0, shipPanel.getWidth(), shipPanel.getHeight()/3);	
		nextShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ship < Selector.noOfShips) {
					ship += 1;
				} else {
					ship = 1;
				}
				updateShipIcon();
				shipIcon.update();
				shipStats = Selector.getShipStats(ship);
			}
		});
		shipIcon = new IconLabel(makeSprites(ship),0,(shipPanel.getHeight()/3), shipPanel.getWidth(), shipPanel.getHeight()/3);
		shipIcon.setHorizontalAlignment(SwingConstants.CENTER);
		shipInfo = new HoverPopUp();
		shipIcon.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        shipInfo.setVisible(true);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	shipInfo.setVisible(false);
		    }
		});
		shipStats = Selector.getShipStats(ship);
		shipInfo.setText("Health: " + shipStats[0] + "\n"
				+ "Attack: " + shipStats[1] + "\n"
				+ "Defence: " + shipStats[2] + "\n"
				+ "Max Velocity: " + shipStats[3] + "\n"
				+ "Acceleration: " + shipStats[4]);
		shipInfo.setBounds(playerSelectorPanel.getX() + shipIcon.getX() + (shipIcon.getWidth()/2),
				playerSelectorPanel.getY() + shipIcon.getY()+(shipIcon.getHeight()/2),150,110);
		this.add(shipInfo);
		prevShip = new BattleButton(2, 0, 2*(shipPanel.getHeight()/3), shipPanel.getWidth(), shipPanel.getHeight()/3);
		prevShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ship <= 1) {
					ship = Selector.noOfShips;
				} else {
					ship -= 1;
				}
				updateShipIcon();
				shipIcon.update();
				shipStats = Selector.getShipStats(ship);
			}
		});
		
		shipPanel.add(nextShip);
		shipPanel.add(shipIcon);
		shipPanel.add(prevShip);
		playerSelectorPanel.add(shipPanel);
	}
	
	 void addSkill1Selector(int x, int y, int width, int height) {
		JPanel skill1Panel = new JPanel();
		skill1Panel.setBounds(x, y, width, height);
		skill1Panel.setBackground(new Color(0,0,0,0));
		skill1Panel.setLayout(new GridLayout(0, 1, 0, 0));
		nextSkill1 = new BattleButton(1, 0, 0, skill1Panel.getWidth(), skill1Panel.getHeight()/3);
		skill1Icon = new IconLabel(skill1Cycle[0].getSkillIcon(), 0, skill1Panel.getHeight()/3, skill1Panel.getWidth(), skill1Panel.getHeight()/3);
		nextSkill1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill1 < skill1Cycle.length-1) {
					skill1 += 1;
				} else {
					skill1 = 0;
				}
				updateSkill1Icon();
				skill1Icon.update();
			}
		});
		skill1Info = new HoverPopUp();
		skill1Info.setText(AvailableSkill.getSkillDescription(skill1Cycle[skill1].id));
		skill1Info.setBounds(playerSelectorPanel.getX() + skill1Panel.getX()+(skill1Icon.getWidth()/2)+skill1Icon.getX(),
				playerSelectorPanel.getY() + skill1Panel.getY()+(skill1Icon.getHeight()/2)+skill1Icon.getY(), 150, 150);
		this.add(skill1Info);
		skill1Icon.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        skill1Info.setVisible(true);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	skill1Info.setVisible(false);
		    }
		});
		prevSkill1 = new BattleButton(2, 0, 2*skill1Panel.getHeight()/3, skill1Panel.getWidth(), skill1Panel.getHeight()/3);
		prevSkill1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill1 <= 0) {
					skill1 = skill1Cycle.length-1;
				} else {
					skill1 -= 1;
				}
				updateSkill1Icon();
				skill1Icon.update();
			}
		});
		skill1Panel.setLayout(new GridLayout(0, 1, 0, 0));
		skill1Panel.add(nextSkill1);
		skill1Panel.add(skill1Icon);
		skill1Panel.add(prevSkill1);
		playerSelectorPanel.add(skill1Panel);
		
		
	}

	 void addSkill2Selector(int x, int y, int width, int height) {
		JPanel skill2Panel = new JPanel();
		skill2Panel.setBounds(x, y, width, height);
		skill2Panel.setBackground(new Color(0,0,0,0));
		skill2Panel.setLayout(new GridLayout(0, 1, 0, 0));
		skill2Icon = new IconLabel(skill2Cycle[0].getSkillIcon(), 0, skill2Panel.getHeight()/3, skill2Panel.getWidth(), skill2Panel.getHeight()/3);
		nextskill2 = new BattleButton(1, 0, 0, skill2Panel.getWidth(), skill2Panel.getHeight()/3);
		nextskill2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill2 < skill2Cycle.length-1) {
					skill2 += 1;
				} else {
					skill2 = 0;
				}
				updateSkill2Icon();
				skill2Icon.update();
			}
		});
			skill2Info = new HoverPopUp();
		skill2Info.setText(AvailableSkill.getSkillDescription(skill2Cycle[skill2].id));
		skill2Info.setBounds(playerSelectorPanel.getX() + skill2Panel.getX()+(skill2Icon.getWidth()/2)+skill2Icon.getX(),
				playerSelectorPanel.getY() + skill2Panel.getY()+(skill2Icon.getHeight()/2)+skill2Icon.getY(), 150, 150);
		this.add(skill2Info);
		skill2Icon.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        skill2Info.setVisible(true);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	skill2Info.setVisible(false);
		    }
		});

		prevskill2 = new BattleButton(2, 0, 2*skill2Panel.getHeight()/3, skill2Panel.getWidth(), skill2Panel.getHeight()/3);
		prevskill2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill2 <= 0) {
					skill2 = skill2Cycle.length-1;
				} else {
					skill2 -= 1;
				}
				updateSkill2Icon();
				skill2Icon.update();
			}
		});
		skill2Panel.add(nextskill2);
		skill2Panel.add(skill2Icon);
		skill2Panel.add(prevskill2);
		playerSelectorPanel.add(skill2Panel);
		
		
	}
	
	 void addSkill3Selector(int x, int y, int width, int height) {
		JPanel skill3Panel = new JPanel();
		skill3Panel.setBounds(x, y, width, height);
		skill3Panel.setBackground(new Color(0,0,0,0));
		skill3Panel.setLayout(new GridLayout(0, 1, 0, 0));
		skill3Icon = new IconLabel(skill3Cycle[0].getSkillIcon(), 0, skill3Panel.getHeight()/3, skill3Panel.getWidth(), skill3Panel.getHeight()/3);
		nextskill3 = new BattleButton(1, 0, 0, skill3Panel.getWidth(), skill3Panel.getHeight()/3);
		nextskill3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill3 < skill3Cycle.length-1) {
					skill3 += 1;
				} else {
					skill3 = 0;
				}
				updateSkill3Icon();
				skill3Icon.update();
			}
		});
		skill3Info = new HoverPopUp();
		skill3Info.setText(AvailableSkill.getSkillDescription(skill3Cycle[skill3].id));
		skill3Info.setBounds(playerSelectorPanel.getX() + skill3Panel.getX()+(skill3Icon.getWidth()/2)+skill3Icon.getX(),
				playerSelectorPanel.getY() + skill3Panel.getY()+(skill3Icon.getHeight()/2)+skill3Icon.getY(), 150, 150);
		this.add(skill3Info);
		skill3Icon.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        skill3Info.setVisible(true);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	skill3Info.setVisible(false);
		    }
		});

		prevskill3 = new BattleButton(2, 0, 2*skill3Panel.getHeight()/3, skill3Panel.getWidth(), skill3Panel.getHeight()/3);
		prevskill3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skill3 <= 0) {
					skill3 = skill3Cycle.length-1;
				} else {
					skill3 -= 1;
					
				}
				updateSkill3Icon();
				skill3Icon.update();
				
			}
		});
		skill3Panel.add(nextskill3);
		skill3Panel.add(skill3Icon);
		skill3Panel.add(prevskill3);
		playerSelectorPanel.add(skill3Panel);	
	}
	
	 void addPlayerSelectors() {
		int space = InputHandler.screenSize.width/100;
		int width = InputHandler.screenSize.width/40;
		playerSelectorPanel = new JPanel();
		playerSelectorPanel.setBounds(back.getX(), InputHandler.screenSize.height/3, 
				4*width + 3*space, InputHandler.screenSize.height/7);
		playerSelectorPanel.setLayout(null);
		playerSelectorPanel.setBackground(new Color(0,0,0,0));
		addShipSelector(0, 0, width, playerSelectorPanel.getHeight());
		addSkill1Selector(width + space, 0, width, playerSelectorPanel.getHeight());
		addSkill2Selector(2*width + 2*space, 0, width, playerSelectorPanel.getHeight());
		addSkill3Selector(3*width + 3*space, 0, width, playerSelectorPanel.getHeight());
		this.add(playerSelectorPanel);
	}
	
	 void addFleetSelector() {
		myFleet = new int[Selector.noOfShips];
		enemyFleet  = new int[Selector.noOfShips];
		for (int i = 0; i < myFleet.length; i++) {
			myFleet[i] = 0;
			enemyFleet[i] = 0;
		}
		int rowHeight = 60;
		int iconWidth = 40;
		int space = 10, x = 0, y = 0;
		JScrollPane scroll = new JScrollPane();
		scroll.setBackground(new Color(0,0,0,0));
		scroll.setBounds(playerSelectorPanel.getX()+playerSelectorPanel.getWidth()+10, playerSelectorPanel.getY(), 
				7*(iconWidth+space), playerSelectorPanel.getHeight()+colourPanel.getHeight());
		JPanel pickerPanel = new JPanel();
		pickerPanel.setBounds(0,0,scroll.getWidth(), Selector.noOfShips*rowHeight);
		pickerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		pickerPanel.setBackground(new Color(0,0,0,0));
		incrementButtons = new Button[Selector.noOfShips];
		decrementButtons = new Button[Selector.noOfShips];
		HoverPopUp info = new HoverPopUp();
		info.setBounds(scroll.getX()+5 + scroll.getWidth(),
				scroll.getY(),150,110);
		this.add(info);
		for (int i = 0; i < Selector.noOfShips;i++) {
			JPanel row = new JPanel();
			int index = i;
			row.setSize(new Dimension(pickerPanel.getWidth(), rowHeight));
			row.setBackground(Color.red);
			row.setLayout(new FlowLayout(FlowLayout.LEFT));
			row.setBackground(new Color(0,0,0,0));
			AvailableSkill[] skills = Selector.getSkills(i+1);
			IconLabel shipIcon = new IconLabel(Selector.getShipImage(i+1,true), x, y, iconWidth, rowHeight);
			x=iconWidth+space;
			IconLabel skill1Icon = new IconLabel(skills[0].getSkillIcon(), x, y, iconWidth, rowHeight);
			x+=x;
			IconLabel skill2Icon = new IconLabel(skills[1].getSkillIcon(), x, y, iconWidth, rowHeight);
			x+=x;
			IconLabel skill3Icon = new IconLabel(skills[2].getSkillIcon(), x, y, iconWidth, rowHeight);
			x+=x;
			
			Label quantity = new Label(String.valueOf(myFleet[i]), "Verdana", Font.BOLD, 20, Color.white, x, y, iconWidth, rowHeight, JLabel.LEFT);
			x+=x;
			Button removeUnit = new Button("-", x, y, iconWidth, rowHeight);
			removeUnit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (myFleet[index] > 0) {
						myFleet[index]--;
					}
					quantity.setText(String.valueOf(myFleet[index]));
				}
			});
			x+=x;
			Button addUnit = new Button("+", 10+100*i, y, iconWidth, rowHeight);
			addUnit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (countShips(myFleet) < maxShips) {
						myFleet[index]++;
					}
					quantity.setText(String.valueOf(myFleet[index]));
				}
			});
			shipIcon.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	int[] shipStats = Selector.getShipStats(index);
					info.setText("Health: " + shipStats[0] + "\n"
							+ "Attack: " + shipStats[1] + "\n"
							+ "Defence: " + shipStats[2] + "\n"
							+ "Max Velocity: " + shipStats[3] + "\n"
							+ "Acceleration: " + shipStats[4]);
			        info.setVisible(true);
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	info.setVisible(false);
			    }
			});
			skill1Icon.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	info.setText(AvailableSkill.getSkillDescription(skills[0].id));
			    	info.setVisible(true);
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	info.setVisible(false);
			    }
			});
			skill2Icon.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	info.setText(AvailableSkill.getSkillDescription(skills[1].id));
			    	info.setVisible(true);
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	info.setVisible(false);
			    }
			});
			skill3Icon.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	info.setText(AvailableSkill.getSkillDescription(skills[2].id));
			    	info.setVisible(true);
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			    	info.setVisible(false);
			    }
			});
			
			incrementButtons[i] = addUnit;
			decrementButtons[i] = removeUnit;
			row.add(shipIcon);
			row.add(skill1Icon);
			row.add(skill2Icon);
			row.add(skill3Icon);
			row.add(removeUnit);
			row.add(quantity);
			row.add(addUnit);
			pickerPanel.add(row);
		}
		scroll.setViewportView(pickerPanel);
	
		this.add(scroll);
	}
	
	 void addColourSelector() {
		colourPanel = new JPanel();
		colourPanel.setBounds(playerSelectorPanel.getX(), playerSelectorPanel.getY()+playerSelectorPanel.getHeight()+20,
				playerSelectorPanel.getWidth(), InputHandler.screenSize.height/7);
		colourPanel.setBackground(new Color(0,0,0,0));
		int labelWidth = 35, x = labelWidth, y = 0, width = colourPanel.getWidth()-x, height = colourPanel.getHeight()/3;
		labelR = new Label("R: ", "Verdana", Font.BOLD, 20, Color.white, 0, y, labelWidth, height, JLabel.LEFT);
		
		colorR = new Slider(x, y, width, height, new Color(0,0,0,0), Color.white, 0, 255, 0, 5, true, false,
				MathHandler.random.nextInt(255));
		colorR.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				playerColor = new Color(colorR.getValue(), colorG.getValue(), colorB.getValue());
				updateShipIcon();
			}
		});
		y = y + height;
		labelG = new Label("G: ", "Verdana", Font.BOLD, 20, Color.white, 0, y, labelWidth, height, JLabel.LEFT);
		colorG = new Slider(x, y, width, height, new Color(0,0,0,0), Color.white, 0, 255, 0, 5, true, false,
				MathHandler.random.nextInt(255));
		colorG.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				playerColor = new Color(colorR.getValue(), colorG.getValue(), colorB.getValue());
				updateShipIcon();
			}
		});
		y = y + height;
		labelB = new Label("B: ", "Verdana", Font.BOLD, 20, Color.white, 0, y, labelWidth, height, JLabel.LEFT);
		colorB = new Slider(x, y, width, height, new Color(0,0,0,0), Color.white, 0, 255, 0, 5, true, false,
			MathHandler.random.nextInt(255));
		colorB.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				playerColor = new Color(colorR.getValue(), colorG.getValue(), colorB.getValue());
				updateShipIcon();
			}
		});
		
		playerColor = new Color(colorR.getValue(), colorG.getValue(), colorB.getValue());
		
		colourPanel.setLayout(null);
		colourPanel.add(labelR);
		colourPanel.add(colorR);
		colourPanel.add(labelG);
		colourPanel.add(colorG);
		colourPanel.add(labelB);
		colourPanel.add(colorB);
		this.add(colourPanel);
	
	}
	
	/**
	 * creates a new instant battle menu
	 * 
	 * @param bg
	 *            the background of the menu
	 */
	public InstantMenu(String bg) {
		super(bg);
		client = null;
		clientThread = null;
		hosting= false;
		connected = false;
		ready = false;
		server = null;
		serverThread = null;
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 10;
		int height = InputHandler.screenSize.height / 14;
		int x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		int y = (int) ((InputHandler.midPoint.y / 2) * 3 - (height / 2));
		Label title = new Label("Instant Battle", "Verdana", Font.BOLD, 50, Color.BLUE,
				(int) InputHandler.midPoint.x - 250, 200,500,40, Label.CENTER);
		back = new Button("Back", x, y, width, height);
		this.add(title);
		initialiseSelectors();
		addPlayerSelectors();
		x = (int) ((InputHandler.midPoint.x / 2) * 3 - (width / 2));
		serverIP = new Label("", "Verdana", Font.BOLD, 20, Color.white, back.getX()+10, playerSelectorPanel.getY() - 75, 300, 30, JLabel.LEFT);
		serverPort = new Label("", "Verdana", Font.BOLD, 20, Color.white, back.getX()+10, playerSelectorPanel.getY() - 40, 300, 30, JLabel.LEFT);
		connect = new Button("Connect",(int)(3*(InputHandler.screenSize.getWidth()/4) - (width/2)), y-100,
				width, height);
		status = new Label("", "Verdana", Font.BOLD, 20, Color.white, connect.getX()-40,(int)InputHandler.midPoint.y - 80 , 300, 30, JLabel.LEFT);
		Label ipLabel = new Label("IP:", "Verdana", Font.BOLD, 20, Color.white, connect.getX()-connect.getWidth()+100,(int)InputHandler.midPoint.y - 50, 100, 30, JLabel.LEFT);
		Label portLabel = new Label("Port:", "Verdana", Font.BOLD, 20, Color.white, ipLabel.getX(), ipLabel.getY() + 40, 100, 30, JLabel.LEFT);
		this.add(ipLabel);
		this.add(portLabel);
		ip = new TextBox("", ipLabel.getX()+ipLabel.getWidth(), ipLabel.getY(), 250, 30);
		port = new TextBox("49152", portLabel.getX()+portLabel.getWidth(), portLabel.getY(), 250, 30);
		this.add(ip);
		this.add(port);
		serverIP.setVisible(false);
		serverPort.setVisible(false);
		this.add(status);
		this.add(serverIP);
		this.add(serverPort);
		host = new Button("Host", (int) InputHandler.midPoint.x - width/2, y, width, height);
		host.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hosting) {
					server.changeState(ServerState.WAITING);
					server.kill();
					client.leave();
					client.kill();
					client = null;
					server = null;
					serverThread.stop();
					serverThread = null;

					clientThread.stop();
					clientThread = null;
					host.setText("Host");
					start.setVisible(false);
					ip.setVisible(true);
					ipLabel.setVisible(true);
					portLabel.setVisible(true);
					port.setVisible(true);
					connect.setVisible(true);
					serverIP.setVisible(false);
					serverPort.setVisible(false);
					status.setVisible(false);
					hosting = false;
				} else {
					server = new BattleModeServer();
					serverThread = new Thread(server);
					serverThread.start();
					Battle.myTeam = 1;
					serverIP.setText("IP: "+server.getIP());
					serverPort.setText("Port: "+server.getPort());
					serverIP.setVisible(true);
					serverPort.setVisible(true);
					try {
					client = new BattleModeClient(1, "server", server.getIP(), server.getPort());
					clientThread = new Thread(client);
					clientThread.start();
					hosting = true;
					start.setText("Play");
					status.setText("Waiting for connection");
					status.setVisible(true);
					ipLabel.setVisible(false);
					portLabel.setVisible(false);
					ip.setVisible(false);
					port.setVisible(false);
					connect.setVisible(false);
					host.setText("Disconnect");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
						    "Could not connect to the server.",
						    "Connection Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				}
			}
		});
		this.add(host);
		start = new Button("Play", (int)(3*(InputHandler.screenSize.getWidth()/4) - (width/2)),
				y, width, height);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hosting && server.getState() == ServerState.READY || connected) {
					if (hosting || !ready ) {
					int team = 0;
					if (hosting) {
						team = 1;
					} else {
						team = 2;
					}
					int[] player = new int[]{ship,skill1Cycle[skill1].getSkillID(),
							skill2Cycle[skill2].getSkillID(),skill3Cycle[skill3].getSkillID(),team};
					myShipSprites = makeSprites(ship);
					client.sendReady(myFleet,player,playerColor);
					prevskill3.setVisible(false);
					 nextskill3.setVisible(false);
					 prevskill2.setVisible(false);
					 nextskill2.setVisible(false);
					 prevSkill1.setVisible(false);
					 nextSkill1.setVisible(false);
					 prevShip.setVisible(false);
					 if (start.getText() == "Ready") {
							start.setVisible(false);
						}
					 nextShip.setVisible(false);
					for (int i = 0; i < Selector.noOfShips; i++) {
						incrementButtons[i].setVisible(false);
						decrementButtons[i].setVisible(false);
					}
					ready = true;
					} else if (ready) {
						prevskill3.setVisible(true);
						 nextskill3.setVisible(true);
						 prevskill2.setVisible(true);
						 nextskill2.setVisible(true);
						 prevSkill1.setVisible(true);
						 nextSkill1.setVisible(true);
						 prevShip.setVisible(true);
						 nextShip.setVisible(true);
						for (int i = 0; i < Selector.noOfShips; i++) {
							incrementButtons[i].setVisible(true);
							decrementButtons[i].setVisible(true);
						}
						ready = false;
					}
				}
			}
		});
		start.setVisible(false);
		this.add(start);
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connected) {
					connect.setText("Connect");
					start.setVisible(false);
					host.setVisible(true);
					ip.setVisible(true);
					status.setVisible(false);
					port.setVisible(true);
					ipLabel.setVisible(true);
					portLabel.setVisible(true);
					connected = false;
				} else {
					try {
					client = new BattleModeClient(2, "server", ip.getText(), Integer.parseInt(port.getText()));
					clientThread = new Thread(client);
					clientThread.start();
					connected = true;
					Battle.myTeam = 2;
					start.setText("Ready");
					status.setText("Connected");
					status.setVisible(true);
					start.setVisible(true);
					ipLabel.setVisible(false	);
					portLabel.setVisible(false);
					ip.setVisible(false);
					port.setVisible(false);
					host.setVisible(false);
					connect.setText("Disconnect");
					start.setVisible(true);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
							    "Could not connect to the server.",
							    "Connection Error",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
				
				
			}
		});
		this.add(back);
		this.add(connect);
		addColourSelector();
		addFleetSelector();
		makeSprites(ship);
		updateShipIcon();
		
		
	}
	
	 int countShips(int[] array) {
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	public void updateShipIcon() {
		shipStats = Selector.getShipStats(ship);
		shipInfo.setText("Health: " + shipStats[0] + "\n"
				+ "Attack: " + shipStats[1] + "\n"
				+ "Defence: " + shipStats[2] + "\n"
				+ "Max Velocity: " + shipStats[3] + "\n"
				+ "Acceleration: " + shipStats[4]);
		
		shipIcon.setSprites(makeSprites(ship));
	}
	
	public void updateSkill1Icon() {
		skill1Info.setText(AvailableSkill.getSkillDescription(skill1Cycle[skill1].id));
		skill1Icon.setSprite(skill1Cycle[skill1].getSkillIcon());
	}
	public void updateSkill2Icon() {
		skill2Info.setText(AvailableSkill.getSkillDescription(skill2Cycle[skill2].id));
		
		skill2Icon.setSprite(skill2Cycle[skill2].getSkillIcon());
		
	}
	public void updateSkill3Icon() {
		skill3Info.setText(AvailableSkill.getSkillDescription(skill3Cycle[skill3].id));
		skill3Icon.setSprite(skill3Cycle[skill3].getSkillIcon());
		
	}
		
	void doServerUpdate() {
		if (BattleModeServer.stateChanged) {
			if (server != null) {
			switch (server.getState()) {
				case WAITING:
					status.setText("Waiting for connection");
					break;
				case CONNECTED:
					status.setText("Waiting for ready");
					start.setVisible(true);
					break;
				case READY:
					status.setText("Ready to Play");
					start.setVisible(true);
					break;
				case PLAYING:
					break;
			case INITIALISING:
				break;
			default:
				break;
					
				
			}
		}
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!controlsClosed && !controls.clicked(InputHandler.getMousePositionOnScreen())) {
			g.setFont(new Font("Verdana",Font.BOLD,16));
			controls.draw((Graphics2D) g);
		} else {
			controlsClosed = true;
		}
	}
	
	
	@Override
	public int update(float time) {
		repaint();
		if (hosting) {
			doServerUpdate();
		}
		shipIcon.update();
		if (back.buttonPressed()) {
			return State.MAIN;
		}
		if (client != null && client.getState() == ServerState.PLAYING) {
			return State.INSTANT;
		}
		
		return 0;
	}

	/**
	 * creates some player sprites based on the colour chosen by the user
	 * 
	 * @return a bufferedimage
	 */
	public Image[] makeSprites(int type) {
		base = Selector.getShipSpriteSheet(type);
		overlay = Selector.getShipOverlay(type);
		player = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = player.getGraphics();
		for (int i = 0; i < overlay.getWidth(); i++) {
			for (int j = 0; j < overlay.getHeight(); j++) {
				if (overlay.getRGB(i, j) == prevColor.getRGB() || overlay.getRGB(i, j) == Color.WHITE.getRGB()) {
					overlay.setRGB(i, j, playerColor.getRGB());
				}
			}
		}
		prevColor = playerColor;
		g.drawImage(base, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);
		Dimension size = Selector.getShipSize(type);
		return ResourceHandler.convertSpriteSheet(player, size.width, size.height);
	}

	public static BattleModeClient getClient() {
		return client;
	}
}
