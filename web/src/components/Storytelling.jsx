import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";
import { cn } from "../utils/cn";

export default function Storytelling() {
  const containerRef = useRef(null);
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start end", "end start"]
  });

  return (
    <section id="vision" ref={containerRef} className="py-48 relative bg-white">
      <div className="max-w-4xl mx-auto px-margin-mobile text-center">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true, margin: "-100px" }}
          transition={{ duration: 1 }}
        >
          <span className="text-primary-container font-bold tracking-widest text-xs uppercase mb-6 inline-block">The Paradigm Shift</span>
        </motion.div>
        
        <h2 className="font-headline-xl text-4xl md:text-6xl text-primary leading-tight tracking-tight mb-12">
          Sustainability as the <br/>
          <span className="text-text-secondary line-through decoration-error/50">Option</span>{" "}
          <span className="text-primary-container italic">Standard.</span>
        </h2>
        
        <div className="space-y-8 text-xl md:text-2xl text-text-secondary leading-relaxed font-light">
          <motion.p
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ duration: 1, delay: 0.2 }}
          >
            Current ride platforms treat green energy as a niche add-on. You pay extra for low-impact travel, while the default contributes to urban smog.
          </motion.p>
          <motion.p
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ duration: 1, delay: 0.4 }}
            className="text-primary font-medium"
          >
            Every single vehicle in our fleet is 100% electric. No exceptions. No compromise. Premium travel that repairs the city's air with every kilometer.
          </motion.p>
        </div>
      </div>
    </section>
  );
}
