import { motion, useInView } from "framer-motion";
import { useRef, useEffect, useState } from "react";

function Counter({ from, to, duration, label }) {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });
  const [count, setCount] = useState(from);

  useEffect(() => {
    if (isInView) {
      let startTimestamp = null;
      const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / (duration * 1000), 1);
        setCount(Math.floor(progress * (to - from) + from));
        if (progress < 1) {
          window.requestAnimationFrame(step);
        }
      };
      window.requestAnimationFrame(step);
    }
  }, [isInView, from, to, duration]);

  return (
    <div ref={ref} className="p-8 md:p-16 border-r last:border-r-0 border-glass-border/30">
      <div className="text-5xl md:text-8xl font-extrabold text-primary mb-6 tracking-tighter">
        {count.toLocaleString()}
      </div>
      <div className="text-text-secondary font-bold uppercase tracking-widest text-sm md:text-base">
        {label}
      </div>
    </div>
  );
}

export default function Impact() {
  return (
    <section id="impact" className="py-48 relative border-t border-glass-border/30 bg-white">
      <div className="max-w-container-max mx-auto px-margin-mobile md:px-gutter relative z-10">
        <div className="text-center mb-24">
          <motion.h2 
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="font-headline-xl text-4xl md:text-6xl text-primary"
          >
            Your Contribution, <span className="text-secondary italic">Quantified.</span>
          </motion.h2>
        </div>
        
        <div className="glass-panel rounded-[3rem] md:rounded-[4rem] border border-secondary/20 overflow-hidden backdrop-blur-3xl bg-surface-container-lowest/80">
          <div className="grid grid-cols-1 md:grid-cols-3 divide-y md:divide-y-0 md:divide-x divide-glass-border/30">
            <Counter from={0} to={12850} duration={2} label="Kg CO₂ Saved" />
            <Counter from={0} to={84200} duration={2.5} label="Cleaner Rides Today" />
            <Counter from={0} to={540} duration={2} label="Tree Equivalents" />
          </div>
        </div>
      </div>
    </section>
  );
}
