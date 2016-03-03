using EPL.ModelData.Symmetrix.VMAX.VMAX3.Workload;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Google.Protobuf;
namespace SMRExecuter
{
    static class AddWorkload
    {
        public static void AddToFile(String FilePath, WorkloadTimeStamps compWL)
        {
            WorkloadTimeStampsProto workloadProto = new WorkloadTimeStampsProto();
            workloadProto.Timestamps.Add(compWL.getUnixTimes());
            workloadProto.SymmetrixId = compWL.getSymmetrixID();

            foreach (var data in compWL.GetData())
            {
                KPICategory kPICategory = (KPICategory)Enum.Parse(typeof(KPICategory), data.Key.ToString());
                KPIComponent kPIComponent = new KPIComponent();
                kPIComponent.Category = kPICategory;
                foreach (var instance in data.Value.getInstances())
                {
                    KPIInstance kPIInstance = new KPIInstance();
                    kPIInstance.Name = instance.Value.getName();
                    kPIInstance.Category = kPICategory;
                    foreach (var kPIMetricDoubleArrayPair in instance.Value.getValues())
                    {
                        KPIMetricDoubleArrayPair pair = new KPIMetricDoubleArrayPair();
                        pair.Key = (KPIMetric)Enum.Parse(typeof(KPIMetric), kPIMetricDoubleArrayPair.Key.ToString());
                        pair.Value = new DoubleArray();
                        pair.Value.Array.Add(kPIMetricDoubleArrayPair.Value);
                        kPIInstance.Values.Add(pair);

                    }
                    kPIComponent.Instances.Add(instance.Key, kPIInstance);
                }
                foreach (var metric in data.Value.getMetrics())
                {
                    kPIComponent.Metrics.Add((KPIMetric)Enum.Parse(typeof(KPIMetric), metric.ToString()));
                }
                KPICategoryKPIComponentPair kPICategoryKPIComponentPair = new KPICategoryKPIComponentPair();
                kPICategoryKPIComponentPair.Key = kPICategory;
                kPICategoryKPIComponentPair.Value = kPIComponent;
                workloadProto.Data.Add(kPICategoryKPIComponentPair);
            }

            using (Stream output = File.OpenWrite(FilePath))
            {
                workloadProto.WriteTo(output);
            }

        }
    }
}
