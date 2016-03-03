using EPL.ModelData.Symmetrix.VMAX.Shared.Configuration;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Google.Protobuf;
using EPL.ModelData.Symmetrix.VMAX.VMAX3.Configuration;

namespace SMRExecuter
{
    static class AddConfiguration
    {
        public static void AddToFile(String FilePath, IStorageArrayConfiguration configuration)
        {
            VMAX3Configuration v3configToPaser = configuration as VMAX3Configuration;
            //initialize proto class from data file or create a new one if none.
             VMAX3ConfigurationProto v3configProto = new VMAX3ConfigurationProto();     
            //Model
            v3configProto.Model = v3configToPaser.GetModel();
            //Sn
            v3configProto.Sn = v3configToPaser.GetSerialNumber();
            //Engine count
            v3configProto.EngineCount = v3configToPaser.GetEngineCount();
            //ucode major
            v3configProto.UcodeMajor = v3configToPaser.GetuCodeMajorVersion();
            //ucode minor
            v3configProto.UcodeMinor = v3configToPaser.GetuCodeMinorVersion();
            //Boards
            List<BoardData> boardtoadd = new List<BoardData>();
            foreach (var item in v3configToPaser.GetBoards())
            {
                BoardData itemtoadd = new BoardData();
                itemtoadd.Name = item.GetName();
                boardtoadd.Add(itemtoadd);
            }
            v3configProto.Boards.Add(boardtoadd);
            //FeChans
            List<FeChannelData> fechantoadd = new List<FeChannelData>();
            foreach (var item in v3configToPaser.GetFeChannels())
            {
                FeChannelData itemtoadd = new FeChannelData();
                itemtoadd.ChanType = item.GetChannelType();
                itemtoadd.Compression = item.GetCompression();
                itemtoadd.Index = item.GetIndex();
                itemtoadd.Name = item.GetName();
                itemtoadd.SpeedLimit = item.GetSpeedLimit();
                fechantoadd.Add(itemtoadd);
            }
            v3configProto.FeChans.Add(fechantoadd);
            //BeChans
            List<BeChannelData> bechantoadd = new List<BeChannelData>();
            foreach (var item in v3configToPaser.GetBeChannels())
            {
                BeChannelData itemtoadd = new BeChannelData();
                itemtoadd.ChanType = item.GetChannelType();
                itemtoadd.Compression = item.GetCompression();
                itemtoadd.Index = item.GetIndex();
                itemtoadd.Name = item.GetName();
                itemtoadd.SpeedLimit = item.GetSpeedLimit();
                bechantoadd.Add(itemtoadd);
            }
            v3configProto.BeChans.Add(bechantoadd);
            //RdfChans
            List<RdfChannelData> rdfchantoadd = new List<RdfChannelData>();
            foreach (var item in v3configToPaser.GetRdfChannels())
            {
                RdfChannelData itemtoadd = new RdfChannelData();
                itemtoadd.ChanType = item.GetChannelType();
                itemtoadd.Compression = item.GetCompression();
                itemtoadd.Index = item.GetIndex();
                itemtoadd.Name = item.GetName();
                itemtoadd.SpeedLimit = item.GetSpeedLimit();
                rdfchantoadd.Add(itemtoadd);
            }
            v3configProto.RdfChans.Add(rdfchantoadd);
            //DiskData
            List<DiskData> diskstoadd = new List<DiskData>();
            foreach (var item in v3configToPaser.GetDisks())
            {
                DiskData itemtoadd = new DiskData();
                itemtoadd.Diskgroup = item.GetDiskGroupName();
                itemtoadd.IsSpare = item.isSpare();
                itemtoadd.Model = item.GetModel();
                itemtoadd.Name = item.GetName();
                itemtoadd.Spindle = item.GetSpindleId();
                diskstoadd.Add(itemtoadd);
            }
            v3configProto.Disks.Add(diskstoadd);
            //RdfGroups
            List<RdfGroupData> rfggroupstoadd = new List<RdfGroupData>();
            foreach (var item in v3configToPaser.GetRdfGroups())
            {
                RdfGroupData itemtoadd = new RdfGroupData();
                itemtoadd.Name = item.GetName();
                rfggroupstoadd.Add(itemtoadd);
            }
            v3configProto.RdfGroups.Add(rfggroupstoadd);
            //slos
            List<SLOData> slostoadd = new List<SLOData>();
            foreach (var item in v3configToPaser.GetSLOs())
            {
                SLOData itemtoadd = new SLOData();
                itemtoadd.Name = item.GetName();
                itemtoadd.High = (int)item.GetHigh();
                itemtoadd.Low = (int)item.GetLow();
                itemtoadd.Rank = item.GetRank();
                slostoadd.Add(itemtoadd);
            }
            v3configProto.Slos.Add(slostoadd);

            //VMAX3DataPoolData
            List<VMAX3DataPoolData> datapooltoadd = new List<VMAX3DataPoolData>();
            foreach (var item in v3configToPaser.GetDataPools())
            {
                VMAX3DataPoolData itemtoadd = new VMAX3DataPoolData();
                itemtoadd.DiskgroupName = item.getDiskGroupName();
                itemtoadd.Name = item.GetName();
                itemtoadd.Prot = item.GetProtection();
                itemtoadd.Srp = item.GetSRP();
                itemtoadd.Tdats.Add(item.getTdatNames());
                itemtoadd.Thins.Add(item.getThinNames());
                datapooltoadd.Add(itemtoadd);
            }
            v3configProto.Datapools.Add(datapooltoadd);

            //ThinDevs
            List<ThinDeviceData> ThinDevstoadd = new List<ThinDeviceData>();
            foreach (var item in v3configToPaser.GetThinDevices())
            {
                ThinDeviceData itemtoadd = new ThinDeviceData();
                itemtoadd.IsMainFrame = item.GetIsMainframe();
                itemtoadd.Name = item.GetName();
                itemtoadd.SizeInCyl = item.GetSizeInCylinders();
                ThinDevstoadd.Add(itemtoadd);
            }
            v3configProto.ThinDevs.Add(ThinDevstoadd);

            //ChanBoardMp
            v3configProto.ChanBoardMp.Add(v3configToPaser.GetChannelBoardMapping());

            //TDats
            List<DataDeviceData> TDatstoadd = new List<DataDeviceData>();
            foreach (var item in v3configToPaser.GetDataDevices())
            {
                DataDeviceData itemtoadd = new DataDeviceData();
                itemtoadd.LocalProtection = item.LocalProtection;
                itemtoadd.Name = item.GetName();
                itemtoadd.SizeInGigaBytes = item.SizeInGigaBytes;
                TDatstoadd.Add(itemtoadd);
            }
            v3configProto.Tdats.Add(TDatstoadd);          
 
            //Edisks
            List<EDiskData> Ediskstoadd = new List<EDiskData>();
            foreach (var item in v3configToPaser.GetEDisks())
            {
                EDiskData itemtoadd = new EDiskData();
                itemtoadd.Diskgroup = item.GetDiskGroupName();
                itemtoadd.IsSpare = item.isSpare();
                itemtoadd.Model = item.GetModel();
                itemtoadd.Name = item.GetName();
                itemtoadd.Spindle = item.GetSpindleId();
                Ediskstoadd.Add(itemtoadd);
            }
            v3configProto.Edisks.Add(Ediskstoadd);          

            //srps
            List<SRPData> srptoadd = new List<SRPData>();
            foreach (var item in v3configToPaser.GetSRPs())
            {
                SRPData itemtoadd = new SRPData();
                itemtoadd.Name = item.GetName();
                srptoadd.Add(itemtoadd);
            }
            v3configProto.Srps.Add(srptoadd);
            
            //StorageGroup
            List<StorageGroupData> sgtoadd = new List<StorageGroupData>();
            foreach (var item in v3configToPaser.GetStorageGroups())
            {
                StorageGroupData itemtoadd = new StorageGroupData();
                itemtoadd.Name = item.GetName();
                sgtoadd.Add(itemtoadd);
            }
            v3configProto.Sgroups.Add(sgtoadd);

            //DiskGroup--bug
            List<DiskGroupData> dgtoadd = new List<DiskGroupData>();
            foreach (var item in v3configToPaser.GetDiskGroups())
            {
                DiskGroupData itemtoadd = new DiskGroupData();
                itemtoadd.Name = item.GetName();
               // itemtoadd.Model
                itemtoadd.SizeInGb = item.GetSizeInGB();
                dgtoadd.Add(itemtoadd);
            }
            v3configProto.DiskGroups.Add(dgtoadd);

            //DevFeMap
            foreach (var item in v3configToPaser.GetThinDevicesInFeChannel())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.DevFeMap.Add(item.Key,list);
            }

            //DiskBeMap
            foreach (var item in v3configToPaser.GetDiskBeChannels())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.DiskBeMap.Add(item.Key, list);
            }

            //EdiskFeMap
            foreach (var item in v3configToPaser.GetEDiskFeChannels())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.EdiskFeMap.Add(item.Key, list);
            }

            //SgDevMap
            foreach (var item in v3configToPaser.GetStorageGroupDevices())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.SgDevMap.Add(item.Key, list);
            }

            //RdfGroupsInRdfChannel
            foreach (var item in v3configToPaser.GetRdfGroupsInRdfChannel())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.RdfgroupChanMap.Add(item.Key, list);
            }

            //RdfGroupDevices
            foreach (var item in v3configToPaser.GetRdfGroupDevices())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.RdfgroupDevMap.Add(item.Key, list);
            }

            //StorageGroupSlos
            foreach (var item in v3configToPaser.GetStorageGroupSlos())
            {
                StringList list = new StringList();
                list.List.Add(item.Value);
                v3configProto.SgroupSloMap.Add(item.Key, list);
            }


            using (Stream output = File.OpenWrite(FilePath))
            {
                v3configProto.WriteTo(output);
            }
        }
    }
}
