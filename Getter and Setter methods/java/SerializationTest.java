package com.emc.performance.epl.serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.BeChannelData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.BoardData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.DataDeviceData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.DiskData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.DiskGroupData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.EDiskData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.FeChannelData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.RdfChannelData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.RdfGroupData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.StorageGroupData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.shared.configuration.ThinDeviceData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.configuration.SLOData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.configuration.SRPData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.configuration.VMAX3Configuration;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.configuration.VMAX3DataPoolData;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.workload.KPICategory;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.workload.KPIComponent;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.workload.KPIInstance;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.workload.KPIMetric;
import com.emc.performance.epl.modeldata.symmetrix.vmax.vmax3.workload.WorkloadTimeStamps;
import com.emc.performance.epl.serialization.VMAX3ConfigurationProtoOuterClass.StringList;
import com.emc.performance.epl.serialization.VMAX3ConfigurationProtoOuterClass.VMAX3ConfigurationProto;
import com.emc.performance.epl.serialization.WorkloadStampsProto.KPICategoryKPIComponentPair;
import com.emc.performance.epl.serialization.WorkloadStampsProto.KPIMetricDoubleArrayPair;
import com.emc.performance.epl.serialization.WorkloadStampsProto.WorkloadTimeStampsProto;

public class SerializationTest {
	@Test
	public void TestSerialization() {
		loadConfigurationProtocDataFileTest("C:\\Projects\\V3Config.data");
		loadWorkloadProtocDataFileTest("C:\\Projects\\Workload.data");
	}

	private void loadConfigurationProtocDataFileTest(String filePath) {
		try {
			VMAX3ConfigurationProto v3configProto = VMAX3ConfigurationProto
					.parseFrom(new FileInputStream(filePath));

			VMAX3Configuration config = new VMAX3Configuration();
			// Model
			config.SetModel(v3configProto.getModel());
			// Sn
			config.SetSerialNumber(v3configProto.getSn());
			// engine count
			config.SetEngineCount(v3configProto.getEngineCount());
			// ucode major
			config.SetuCodeMajorVersion(v3configProto.getUcodeMajor());
			// ucode minor
			config.SetuCodeMinorVersion(v3configProto.getUcodeMinor());
			// Boards
			List<BoardData> listBoards = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.BoardData item : v3configProto
					.getBoardsList()) {
				BoardData newItem = new BoardData(item.getName());
				listBoards.add(newItem);
			}
			config.GetBoards().addAll(listBoards);
			// FeChans
			List<FeChannelData> listFeChans = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.FeChannelData item : v3configProto
					.getFeChansList()) {
				FeChannelData newItem = new FeChannelData(item.getName(),
						item.getChanType(), (int) item.getSpeedLimit(),
						item.getIndex(), item.getCompression());

				listFeChans.add(newItem);
			}
			config.GetFeChannels().addAll(listFeChans);

			// BeChans
			List<BeChannelData> listBeChans = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.BeChannelData item : v3configProto
					.getBeChansList()) {
				BeChannelData newItem = new BeChannelData(item.getName(),
						item.getChanType(), (int) item.getSpeedLimit(),
						item.getIndex(), item.getCompression());

				listBeChans.add(newItem);
			}
			config.GetBeChannels().addAll(listBeChans);

			// RdfChans
			List<RdfChannelData> listRdfChans = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.RdfChannelData item : v3configProto
					.getRdfChansList()) {
				RdfChannelData newItem = new RdfChannelData("", item.getName(),
						item.getChanType(), (int) item.getSpeedLimit(),
						item.getIndex(), item.getCompression());

				listRdfChans.add(newItem);
			}
			config.GetRdfChannels().addAll(listRdfChans);

			// DiskData
			List<DiskData> listDisk = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.DiskData item : v3configProto
					.getDisksList()) {
				DiskData newItem = new DiskData(item.getName(),
						item.getModel(), item.getDiskgroup(),
						item.getSpindle(), item.getIsSpare());

				listDisk.add(newItem);
			}
			config.GetDisks().addAll(listDisk);

			// RdfGroups
			List<RdfGroupData> listRdfGroup = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.RdfGroupData item : v3configProto
					.getRdfGroupsList()) {
				RdfGroupData newItem = new RdfGroupData(item.getName());
				listRdfGroup.add(newItem);
			}
			config.GetRdfGroups().addAll(listRdfGroup);

			// slos
			List<SLOData> listSlo = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.SLOData item : v3configProto
					.getSlosList()) {
				SLOData newItem = new SLOData(item.getName(), item.getHigh(),
						item.getLow(), item.getRank());
				listSlo.add(newItem);
			}
			config.GetSLOs().addAll(listSlo);

			// VMAX3DataPoolData
			List<VMAX3DataPoolData> listVMAX3DataPoolData = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.VMAX3DataPoolData item : v3configProto
					.getDatapoolsList()) {
				VMAX3DataPoolData newItem = new VMAX3DataPoolData(
						item.getName(), item.getSrp(), item.getDiskgroupName(),
						item.getProt());
				newItem.getTdatNames().addAll(item.getTdatsList());
				newItem.getThinNames().addAll(item.getThinsList());
				listVMAX3DataPoolData.add(newItem);
			}
			config.GetDataPools().addAll(listVMAX3DataPoolData);

			// ThinDevs
			List<ThinDeviceData> listTDevs = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.ThinDeviceData item : v3configProto
					.getThinDevsList()) {
				ThinDeviceData newItem = new ThinDeviceData(item.getName(),
						item.getSizeInCyl(), item.getIsMainFrame());
				listTDevs.add(newItem);
			}
			config.GetThinDevices().addAll(listTDevs);

			// ChanBoardMp
			config.GetChannelBoardMapping().putAll(
					v3configProto.getChanBoardMp());

			// TDats
			List<DataDeviceData> listTDats = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.DataDeviceData item : v3configProto
					.getTdatsList()) {
				DataDeviceData newItem = new DataDeviceData(item.getName(),
						item.getSizeInGigaBytes(), item.getLocalProtection());
				listTDats.add(newItem);
			}
			config.GetDataDevices().addAll(listTDats);

			// Edisks
			List<EDiskData> listeDisks = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.EDiskData item : v3configProto
					.getEdisksList()) {
				EDiskData newItem = new EDiskData(item.getName(),
						item.getModel(), item.getDiskgroup(),
						item.getSpindle(), item.getIsSpare());

				listeDisks.add(newItem);
			}
			config.GetEDisks().addAll(listeDisks);

			// srps
			List<SRPData> listSrpGroup = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.SRPData item : v3configProto
					.getSrpsList()) {
				SRPData newItem = new SRPData(item.getName());
				listSrpGroup.add(newItem);
			}
			config.GetSRPs().addAll(listSrpGroup);

			// StorageGroup
			List<StorageGroupData> listSg = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.StorageGroupData item : v3configProto
					.getSgroupsList()) {
				StorageGroupData newItem = new StorageGroupData(item.getName());
				listSg.add(newItem);
			}
			config.GetStorageGroups().addAll(listSg);

			// DiskGroup
			List<DiskGroupData> listdg = new ArrayList<>();
			for (VMAX3ConfigurationProtoOuterClass.DiskGroupData item : v3configProto
					.getDiskGroupsList()) {
				DiskGroupData newItem = new DiskGroupData(item.getName(),
						item.getModel(), item.getSizeInGb());
				listdg.add(newItem);
			}
			config.GetDiskGroups().addAll(listdg);

			// DevFeMap
			for (Entry<String, StringList> item : v3configProto.getDevFeMap()
					.entrySet()) {
				config.GetThinDevicesInFeChannel().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// DevBeMap
			for (Entry<String, StringList> item : v3configProto.getDiskBeMap()
					.entrySet()) {
				config.GetDiskBeChannels().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// EdiskFeMap
			for (Entry<String, StringList> item : v3configProto.getEdiskFeMap()
					.entrySet()) {
				config.GetEDiskFeChannels().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// SgDevMap
			for (Entry<String, StringList> item : v3configProto.getSgDevMap()
					.entrySet()) {
				config.GetStorageGroupDevices().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// RdfGroupsInRdfChannel
			for (Entry<String, StringList> item : v3configProto
					.getRdfgroupChanMap().entrySet()) {
				config.GetRdfGroupsInRdfChannel().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// RdfGroupDevices
			for (Entry<String, StringList> item : v3configProto
					.getRdfgroupDevMap().entrySet()) {
				config.GetRdfGroupDevices().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}

			// StorageGroupSlos
			for (Entry<String, StringList> item : v3configProto.getSgDevMap()
					.entrySet()) {
				config.GetStorageGroupSlos().put(item.getKey(),
						new ArrayList<>(item.getValue().getListList()));
			}
			int i=1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadWorkloadProtocDataFileTest(String filePath) {

		try {
			WorkloadTimeStampsProto workloadProto = WorkloadTimeStampsProto
					.parseFrom(new FileInputStream(filePath));

			ArrayList<Long> timestamps = new ArrayList<>();
			timestamps.addAll(workloadProto.getTimestampsList());

			HashMap<KPICategory, KPIComponent> data = new HashMap<>();
			for (KPICategoryKPIComponentPair pair : workloadProto.getDataList()) {
				KPICategory cate = KPICategory.valueOf(pair.getKey().name());
				KPIComponent compo = new KPIComponent(cate);
				for (Entry<String, WorkloadStampsProto.KPIInstance> instPair : pair
						.getValue().getInstances().entrySet()) {
					KPIInstance inst = new KPIInstance(instPair.getValue()
							.getName(), cate);
					for (KPIMetricDoubleArrayPair metricLongArrayPair : instPair
							.getValue().getValuesList()) {
						KPIMetric kPIMetric = KPIMetric
								.valueOf(metricLongArrayPair.getKey().name());
						double[] doubleArray = new double[metricLongArrayPair
								.getValue().getArrayList().size()];
						for (int i = 0; i < doubleArray.length; i++) {
							doubleArray[i] = metricLongArrayPair.getValue()
									.getArrayList().get(i).doubleValue();
						}
						inst.getValues().put(kPIMetric, doubleArray);
					}
					compo.getInstances().put(instPair.getKey(), inst);
				}
				for (WorkloadStampsProto.KPIMetric metric : pair.getValue()
						.getMetricsList()) {
					compo.getMetrics().add(KPIMetric.valueOf(metric.name()));
				}
				data.put(cate, compo);
			}
			WorkloadTimeStamps workload = new WorkloadTimeStamps(timestamps,
					workloadProto.getSymmetrixId(), data);
			workload.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
